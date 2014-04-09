package com.tir38.android.AdjustableSplitPane;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;

public abstract class SingleFragmentActivity extends Activity {

    protected abstract Fragment createFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_fragment);

        // create fragment and add to fm
        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.activity_single_fragment_fragment_container);

        if (fragment == null) {
            Log.d("MyListActivity", "creating fragment");
            fragment = createFragment();
            if (fragment != null) {
                fragmentManager.beginTransaction()
                        .add(R.id.activity_single_fragment_fragment_container, fragment)
                        .commit();
            }
        }
    }
}