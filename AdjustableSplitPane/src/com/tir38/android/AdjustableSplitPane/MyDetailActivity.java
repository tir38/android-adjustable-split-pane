package com.tir38.android.AdjustableSplitPane;

import android.app.Fragment;
import com.tir38.android.AdjustableSplitPane.MyDetailFragment;
import com.tir38.android.AdjustableSplitPane.SingleFragmentActivity;

public class MyDetailActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new MyDetailFragment();
    }
}
