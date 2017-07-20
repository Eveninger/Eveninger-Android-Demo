package com.evening.bezier.qqbubble;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.evening.bezier.R;

public class QQBubbleActivity extends AppCompatActivity {
    public static void start(Context context) {
        Intent starter = new Intent(context, QQBubbleActivity.class);
        context.startActivity(starter);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qqbubble);
    }
}
