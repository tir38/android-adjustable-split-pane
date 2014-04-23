package com.tir38.android.AdjustableSplitPane;

import android.app.Fragment;
import android.content.Intent;

public class MyListActivity extends SingleFragmentActivity implements MyListFragment.Callbacks{

    @Override
    protected Fragment createFragment() {
        return MyListFragment.newInstance();
    }

    @Override
    public void onEmailSelected(Email email) {
        Intent intent = new Intent(this, MyDetailActivity.class);
        intent.putExtra(MyDetailActivity.EXTRA_EMAIL, email);
        startActivity(intent);    }
}
