package com.tir38.android.AdjustableSplitPane;

import android.app.Fragment;
import android.os.Bundle;

public class MyDetailActivity extends SingleFragmentActivity {

    public static final String EXTRA_EMAIL = "MyDetailActivity.EXTRA_EMAIL";

    @Override
    protected Fragment createFragment() {

        // pull email out of intent
        Email email = (Email) getIntent().getSerializableExtra(EXTRA_EMAIL);

        // create fragment
        Fragment fragment = new MyDetailFragment();

        // add email to fragment's arguments
        if (email != null) {
            Bundle args = new Bundle();
            args.putSerializable(MyDetailFragment.ARG_EMAIL, email);
            fragment.setArguments(args);
        }

        return fragment;
    }
}
