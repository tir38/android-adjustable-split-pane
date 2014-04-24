package com.tir38.android.AdjustableSplitPane;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MySplitPaneActivity extends Activity implements MyListFragment.Callbacks {

    private static final String EXTRA_CURRENT_LIST_INDEX = "MySplitPaneActivity.EXTRA_CURRENT_LIST_INDEX";
    private static final String EXTRA_PERCENT_LEFT = "MySplitPaneActivity.EXTRA_PERCENT_LEFT";
    private float mPercentLeft;
    private float mTotalWidth;

    /**
     * public factory
     * @param context
     * @return
     */
    public static Intent newIntent(Context context, float percentLeft) {
        Intent intent = new Intent(context, MySplitPaneActivity.class);
        intent.putExtra(EXTRA_PERCENT_LEFT, percentLeft);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_split_pane);

        // create list fragment and add to fm in the "left pane"
        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.activity_split_pane_left_pane);

        if (fragment == null) {
            fragment = MyListFragment.newInstance();
            if (fragment != null) {
                fragmentManager.beginTransaction()
                        .add(R.id.activity_split_pane_left_pane, fragment)
                        .commit();
            }
        }

        // get percent from save instance state
        mPercentLeft = getIntent().getFloatExtra(EXTRA_PERCENT_LEFT, 50);

        // set weights of left and right pane
        FrameLayout leftPane = (FrameLayout) findViewById(R.id.activity_split_pane_left_pane);
        leftPane.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, mPercentLeft));

        FrameLayout rightPane = (FrameLayout) findViewById(R.id.activity_split_pane_right_pane);
        rightPane.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 100 - mPercentLeft));

        // get screen size
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        mTotalWidth = size.x;

        // set touch listener on divider
        ImageView divider = (ImageView) findViewById(R.id.activity_split_pane_divider);
        divider.setOnTouchListener(new DividerTouchListener());

        // display detail
        // TODO
    }

    private void rebuildLayout(float draggedToX){
        float newPercentLeft = computeNewPercentLeft(draggedToX);

        getIntent().putExtra(EXTRA_PERCENT_LEFT, newPercentLeft);
        finish();
        startActivity(getIntent());
    }

    /**
     * implements MyListFragment's callbacks
     * @param emailIndex
     */
    @Override
    public void onEmailSelected(int emailIndex) {
        // get fragment manager and create new fragment transaction
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // get old (current) fragment
        Fragment oldDetailFragment = fragmentManager.findFragmentById(R.id.activity_split_pane_right_pane);

        // create new fragment
        Email email = DataStore.get().getEmail(emailIndex);
        Fragment newDetailFragment = MyDetailFragment.newInstance(email);

        // if old fragment exists, remove
        if (oldDetailFragment != null) {
            fragmentTransaction.remove(oldDetailFragment);
        }

        // add new fragment (to the right pane) and commit transaction
        fragmentTransaction.add(R.id.activity_split_pane_right_pane, newDetailFragment);
        fragmentTransaction.commit();
    }


    /**
     * Computes the new percent left based on a draggedToX from the touch listener
     */
    private float computeNewPercentLeft(float draggedToX) {
        float percentLeft =  100 - (100*(mTotalWidth - draggedToX)/mTotalWidth);
        Log.d("TAG", "total width = " + mTotalWidth );
        Log.d("TAG", "new percent left = " + percentLeft );
        return percentLeft;
    }

    private class DividerTouchListener implements View.OnTouchListener {

        // note:
        // since I want to return the global coordinate of the touch event, I need to use getRawX(.), not getX(.)

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    break;

                case MotionEvent.ACTION_MOVE:
                    Log.d("DividerTouchListener", "move. dragged to X  = " + event.getRawX());
                    break;

                case MotionEvent.ACTION_UP:
                    rebuildLayout(event.getRawX()); // right now only rebuild fragments when user lifts finger
                    break;

                case MotionEvent.ACTION_CANCEL:
                    break;
            }

            return true;
        }
    }
}