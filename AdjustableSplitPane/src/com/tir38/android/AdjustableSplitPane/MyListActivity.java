package com.tir38.android.AdjustableSplitPane;

import android.app.Fragment;
import android.os.Bundle;

public class MyListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new MyListFragment();
    }
}
