package com.tir38.android.AdjustableSplitPane;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class MySplitPaneActivity extends Activity implements MyListFragment.Callbacks {

    /**
     * public factory
     * @param context
     * @return
     */
    public static Intent newIntent(Context context) {
        return new Intent(context, MySplitPaneActivity.class);
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
    }

    /**
     * implements MyListFragment's callbacks
     * @param email
     */
    @Override
    public void onEmailSelected(Email email) {
        // get fragment manager and create new fragment transaction
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // get old (current) fragment
        // create new fragment
        Fragment oldDetailFragment = fragmentManager.findFragmentById(R.id.activity_split_pane_right_pane);
        Fragment newDetailFragment = MyDetailFragment.newInstance(email);

        // if old fragment exists, remove
        if (oldDetailFragment != null) {
            fragmentTransaction.remove(oldDetailFragment);
        }

        // add new fragment (to the right pane) and commit transaction
        fragmentTransaction.add(R.id.activity_split_pane_right_pane, newDetailFragment);
        fragmentTransaction.commit();
    }
}