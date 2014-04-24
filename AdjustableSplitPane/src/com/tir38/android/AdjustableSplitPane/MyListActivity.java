package com.tir38.android.AdjustableSplitPane;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;

public class MyListActivity extends SingleFragmentActivity implements MyListFragment.Callbacks {

    /**
     * public factory
     * @param context
     * @return
     */
    public static Intent newIntent(Context context) {
        return new Intent(context, MyListActivity.class);
    }

    @Override
    protected Fragment createFragment() {
        return MyListFragment.newInstance();
    }

    /**
     * implements MyListFragment's callbacks
     * @param emailIndex
     */
    @Override
    public void onEmailSelected(int emailIndex) {

        // get email from index
        Email email = DataStore.get().getEmail(emailIndex);

        // create Intent
        Intent intent = MyDetailActivity.newIntent(this, email);
        startActivity(intent);
    }
}
