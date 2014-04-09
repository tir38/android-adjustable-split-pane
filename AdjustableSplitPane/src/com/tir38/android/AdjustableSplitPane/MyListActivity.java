package com.tir38.android.AdjustableSplitPane;

import android.app.Fragment;

public class MyListActivity extends SingleFragmentActivity implements MyListFragment.Callbacks{

    @Override
    protected Fragment createFragment() {
        return MyListFragment.newInstance();
    }

    @Override
    public void onEmailSelected(Email email) {
        // TODO start new detail activity
    }
}
