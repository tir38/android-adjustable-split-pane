package com.tir38.android.AdjustableSplitPane;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        final float initialPercentLeft = 50;
        final int minimumWidth = 250; // dp

        Button regularButton = (Button) findViewById(R.id.activity_home_regular_button);
        regularButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = MyListActivity.newIntent(getApplicationContext());
                startActivity(intent);
            }
        });

        Button splitPaneButton = (Button) findViewById(R.id.activity_home_split_pane_button);
        splitPaneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = MySplitPaneActivity.newIntent(getApplicationContext(), initialPercentLeft, minimumWidth);
                startActivity(intent);
            }
        });
    }
}