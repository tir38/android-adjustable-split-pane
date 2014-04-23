package com.tir38.android.AdjustableSplitPane;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

public class MySplitPaneActivity extends Activity implements MyListFragment.Callbacks {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_split_pane);

        // create fragment and add to fm
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

    @Override
    public void onEmailSelected(Email email) {

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Fragment oldDetailFragment = fragmentManager.findFragmentById(R.id.activity_split_pane_right_pane);
        Fragment newDetailFragment = MyDetailFragment.newInstance();


        // set arguments
        Bundle args = new Bundle();
        args.putSerializable(MyDetailFragment.ARG_EMAIL, email);
        newDetailFragment.setArguments(args);

        if (oldDetailFragment != null) {
            fragmentTransaction.remove(oldDetailFragment);
        }

        fragmentTransaction.add(R.id.activity_split_pane_right_pane, newDetailFragment);
        fragmentTransaction.commit();
    }
}