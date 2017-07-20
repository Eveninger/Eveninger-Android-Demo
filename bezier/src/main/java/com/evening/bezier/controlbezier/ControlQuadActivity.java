package com.evening.bezier.controlbezier;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.evening.bezier.R;

public class ControlQuadActivity extends AppCompatActivity {
    public static void start(Context context) {
        Intent starter = new Intent(context, ControlQuadActivity.class);
        context.startActivity(starter);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_quad);
    }
}
