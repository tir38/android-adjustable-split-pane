package com.tir38.android.AdjustableSplitPane;

import android.app.Fragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;

public class MyListFragment extends ListFragment {

    public static Fragment newInstance() {
        Log.d("MyListFragment", "inside newInstance");
        Fragment fragment = new MyListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MyListFragment", "inside onCreate");
    }
}
