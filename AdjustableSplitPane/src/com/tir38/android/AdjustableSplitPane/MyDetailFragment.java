package com.tir38.android.AdjustableSplitPane;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;


public class MyDetailFragment extends Fragment {

    public static final String ARG_EMAIL = "MyDetailFragment.ARG_EMAIL";
    private Email mEmail;

    public static MyDetailFragment newInstance() {
        MyDetailFragment fragment = new MyDetailFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);    // TODO
        Log.d("MyDetailFragment", "inside onCreate");

        // pull email from args
        mEmail = (Email) getArguments().getSerializable(ARG_EMAIL);
        Log.d("MyDetailFragment", "email from = " + mEmail.getFrom());
    }
}