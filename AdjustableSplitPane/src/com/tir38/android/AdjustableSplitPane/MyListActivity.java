package com.tir38.android.AdjustableSplitPane;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;

public class MyListActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MyListActivity", "inside onCreate");
        setContentView(R.layout.activity_list);

        // create fragment and add to fm
        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.activity_list_fragment_container);

        if (fragment == null) {
            Log.d("MyListActivity", "creating fragment");
            fragment = MyListFragment.newInstance();
            if (fragment != null) {
                fragmentManager.beginTransaction()
                        .add(R.id.activity_list_fragment_container, fragment)
                        .commit();
            }
        }
    }

}
