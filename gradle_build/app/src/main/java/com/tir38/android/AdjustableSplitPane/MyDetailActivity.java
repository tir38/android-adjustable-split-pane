package com.tir38.android.AdjustableSplitPane;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class MyDetailActivity extends SingleFragmentActivity {

    public static final String EXTRA_EMAIL = "MyDetailActivity.EXTRA_EMAIL";

    /**
     * public factory
     * @param context
     * @param email
     * @return
     */
    public static Intent newIntent(Context context, Email email) {
        Intent intent = new Intent(context, MyDetailActivity.class);
        intent.putExtra(EXTRA_EMAIL, email);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        // pull email out of intent
        Email email = (Email) getIntent().getSerializableExtra(EXTRA_EMAIL);

        // create fragment
        Fragment fragment = MyDetailFragment.newInstance(email);
        return fragment;
    }
}
