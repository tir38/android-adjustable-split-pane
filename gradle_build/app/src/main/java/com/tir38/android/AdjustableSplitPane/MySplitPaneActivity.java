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

    private static final String EXTRA_CURRENT_INDEX = "MySplitPaneActivity.EXTRA_CURRENT_INDEX";
    private static final String EXTRA_PERCENT_LEFT = "MySplitPaneActivity.EXTRA_PERCENT_LEFT";
    private static final String EXTRA_MINIMUM_WIDTH_DIP = "MySplitPaneActivity.EXTRA_MINIMUM_WIDTH_DIP";
    private float mTotalWidth; // pixels
    private float mPercentLeft; // percent of screen
    private FrameLayout mRightPane;
    private FrameLayout mLeftPane;
    private float mMinimumWidth; // percent of screen

    /**
     * public factory
     *
     * @param context
     * @param percentLeft
     * @param minimumWidth
     * @return
     */
    public static Intent newIntent(Context context, float percentLeft, int minimumWidth) {
        Intent intent = new Intent(context, MySplitPaneActivity.class);
        intent.putExtra(EXTRA_PERCENT_LEFT, percentLeft);
        intent.putExtra(EXTRA_MINIMUM_WIDTH_DIP, minimumWidth);
        return intent;
    }

    /**
     * Activity's onCreate lifecycle method
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("TAG", "inside onCreate");
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

        // pull extras from intent
        mPercentLeft = getIntent().getFloatExtra(EXTRA_PERCENT_LEFT, 50);
        int minimumWidthDip = getIntent().getIntExtra(EXTRA_MINIMUM_WIDTH_DIP, 100);

        // get screen size
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        mTotalWidth = size.x;

        // convert minimum width from dip to percent
        mMinimumWidth = convertDipToPercent(minimumWidthDip);

        // set touch listener on divider
        ImageView divider = (ImageView) findViewById(R.id.activity_split_pane_divider);
        divider.setOnTouchListener(new DividerTouchListener());

        // get left and right pane and set weights
        mLeftPane = (FrameLayout) findViewById(R.id.activity_split_pane_left_pane);
        mRightPane = (FrameLayout) findViewById(R.id.activity_split_pane_right_pane);
        setWeights(mPercentLeft);

        // display right pane
        int currentIndex = getIntent().getIntExtra(EXTRA_CURRENT_INDEX, 0);
        setupDetailPane(currentIndex);
    }

    /**
     * sets the layout weights of the left and right panes
     *
     * @param percentLeft
     */
    private void setWeights(float percentLeft) {
        Log.d("TAG", "minimum width = " + mMinimumWidth);

        float percentRight = 100 - percentLeft;

        // if left side too small, resize
        if (percentLeft < mMinimumWidth) {
            percentLeft = mMinimumWidth;
            percentRight = 100 - percentLeft;
        }

        // if right side too small, resize
        if (percentRight < mMinimumWidth) {
            percentRight = mMinimumWidth;
            percentLeft = 100 - percentRight;
        }

        // set weights
        mLeftPane.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, percentLeft));
        mRightPane.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, percentRight));
    }

    /**
     * rebuilds entire view by forcing layout pass on root view
     *
     * @param draggedToX
     */
    private void rebuildView(float draggedToX) {
        // reset weights
        mPercentLeft = computeNewPercentLeft(draggedToX);
        setWeights(mPercentLeft);

        // save to extras
        getIntent().putExtra(EXTRA_PERCENT_LEFT, mPercentLeft);

        // force layout pass
        ViewGroup viewGroup = (ViewGroup) findViewById(R.id.activity_split_pane);
        viewGroup.requestLayout();
    }

    /**
     * handles adding/removing detail fragment to fragment manager
     *
     * @param index
     */
    private void setupDetailPane(int index) {
        // get fragment manager and create new fragment transaction
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // get old (current) fragment
        Fragment oldDetailFragment = fragmentManager.findFragmentById(R.id.activity_split_pane_right_pane);

        // create new fragment
        Email email = DataStore.get().getEmail(index);
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
     * Computes the new percent left based on draggedToX from the touch listener
     */
    private float computeNewPercentLeft(float draggedToX) {
        return (100 - (100 * (mTotalWidth - draggedToX) / mTotalWidth));
    }

    /**
     * converts dip dimension to percentage of the screen
     * @param dip
     * @return
     */
    private float convertDipToPercent(int dip) {
        return (dip / mTotalWidth) * 100;
    }

    /**
     * implements MyListFragment's callbacks
     *
     * @param index
     */
    @Override
    public void onItemSelected(int index) {
        setupDetailPane(index);
        // update Extra
        getIntent().putExtra(EXTRA_CURRENT_INDEX, index);
    }

    /**
     * custom TouchListener
     * note:
     * since I want to return the global coordinate of the touch event, I need to use getRawX(.), not getX(.)
     */
    private class DividerTouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    break;

                case MotionEvent.ACTION_MOVE:
                    rebuildView(event.getRawX());
                    break;

                case MotionEvent.ACTION_UP:
                    rebuildView(event.getRawX());
                    break;

                case MotionEvent.ACTION_CANCEL:
                    break;
            }
            return true;
        }
    }
}