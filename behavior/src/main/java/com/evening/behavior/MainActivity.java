package com.evening.behavior;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.evening.behavior.behavior.FollowActivity;
import com.evening.behavior.behavior.ScrollToTopActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView followTV = (TextView) findViewById(R.id.follow);
        TextView scrollToTopTV = (TextView) findViewById(R.id.scrollToTop);

        followTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FollowActivity.start(MainActivity.this);
            }
        });

        scrollToTopTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScrollToTopActivity.start(MainActivity.this);
            }
        });
    }
}
