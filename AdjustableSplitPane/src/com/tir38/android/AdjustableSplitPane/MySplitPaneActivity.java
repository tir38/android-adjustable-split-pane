package com.tir38.android.AdjustableSplitPane;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MySplitPaneActivity extends Activity implements MyListFragment.Callbacks {

    private static final String EXTRA_CURRENT_INDEX = "MySplitPaneActivity.EXTRA_CURRENT_INDEX";
    private static final String EXTRA_PERCENT_LEFT = "MySplitPaneActivity.EXTRA_PERCENT_LEFT";
    private static final String EXTRA_MINIMUM_WIDTH_DIP = "MySplitPaneActivity.EXTRA_MINIMUM_WIDTH_DIP";
    private static final int PADDING_OF_DIVIDER_TOUCH_AREA_DIP = 100;
    private float mTotalWidth; // pixels

    private float mPercentLeft; // percent of screen
    private FrameLayout mRightPane;
    private FrameLayout mLeftPane;
    private float mMinimumWidth; // percent of screen
    private ImageView mDivider;
    private LinearLayout mRootLayout;

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

        mRootLayout = (LinearLayout) findViewById(R.id.activity_split_pane_root_linear_layout);

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

        // get available screen width
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        mTotalWidth = size.x;

        // convert minimum width from dip to percent
        mMinimumWidth = convertDipToPercent(minimumWidthDip);

        // set touch listener on divider
        mDivider = (ImageView) findViewById(R.id.activity_split_pane_divider);
        mDivider.setOnTouchListener(new DividerTouchListener());
        setupTouchDelegate();

        // get left and right pane and set weights
        mLeftPane = (FrameLayout) findViewById(R.id.activity_split_pane_left_pane);
        mRightPane = (FrameLayout) findViewById(R.id.activity_split_pane_right_pane);
        setWeights(mPercentLeft);

        // display right pane
        int currentIndex = getIntent().getIntExtra(EXTRA_CURRENT_INDEX, 0);
        setupDetailPane(currentIndex);

        mRootLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //at this point the layout pass is complete and we can re-set the touch delegate area
                setupTouchDelegate();
            }
        });
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
        ViewGroup viewGroup = (ViewGroup) findViewById(R.id.activity_split_pane_root_linear_layout);
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

    private void setupTouchDelegate() {
        // get bounds of divider
        Rect touchArea = new Rect();
        mDivider.getHitRect(touchArea);

        // increases left and right
        touchArea.left -= PADDING_OF_DIVIDER_TOUCH_AREA_DIP;
        touchArea.right += PADDING_OF_DIVIDER_TOUCH_AREA_DIP;

        // get root view and set TouchDelegate
        if (mRootLayout != null) {
            mRootLayout.setTouchDelegate(new TouchDelegate(touchArea, mDivider));
        }
    }

    /**
     * helper to convert dip dimension to percentage of the screen
     *
     * @param dip
     * @return
     */
    private float convertDipToPercent(int dip) {
        return (dip / mTotalWidth) * 100;
    }

    /**
     * helper to convert percent of the screen into dip dimension
     *
     * @param percent
     * @return
     */
    private int convertPercentToDip(float percent) {
        return (int) ((percent * mTotalWidth) / 100);
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
                    Log.d("TAG", "divider is grabbing this gesture");
                    return true;

                case MotionEvent.ACTION_MOVE:
                    return true;

                case MotionEvent.ACTION_UP:
                    rebuildView(event.getRawX()); // right now only rebuild fragments when user lifts finger
                    return true;

                case MotionEvent.ACTION_CANCEL:
                    return true;

                default:
                    return true;
            }
        }
    }
}