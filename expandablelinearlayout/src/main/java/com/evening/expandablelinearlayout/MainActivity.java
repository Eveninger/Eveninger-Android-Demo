package com.evening.expandablelinearlayout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private ExpandableLinearLayout mExpandableLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mExpandableLinearLayout = (ExpandableLinearLayout) findViewById(R.id.expanded_layout);
        for (int i = 0; i < 5; i++) {
            View view = View.inflate(this, R.layout.item, null);
            mExpandableLinearLayout.addItem(view);
        }
    }
}
