package com.tir38.android.AdjustableSplitPane;

import android.app.Fragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import java.util.List;

public class MyListFragment extends ListFragment {


    private List<Email> mEmails;

    public static Fragment newInstance() {
        Log.d("MyListFragment", "inside newInstance");
        Fragment fragment = new MyListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MyListFragment", "inside onCreate");

        mEmails = DataStore.get().getEmails();


        // create array adapter
        ArrayAdapter<Email> adapter = new ArrayAdapter<Email>(getActivity(), android.R.layout.simple_list_item_1, mEmails);

        setListAdapter(adapter);
    }
}
