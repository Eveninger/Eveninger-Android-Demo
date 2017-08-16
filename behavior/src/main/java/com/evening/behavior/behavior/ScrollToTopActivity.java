package com.evening.behavior.behavior;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.evening.behavior.R;

public class ScrollToTopActivity extends AppCompatActivity {
    public static void start(Context context) {
        Intent starter = new Intent(context, ScrollToTopActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_to_top_behavior);
    }
}
