package com.evening.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView priceTV = (TextView) findViewById(R.id.meal_price);

        KFC kfc = new KFC();
        Meal meal = kfc.order("B");
        priceTV.setText(String.valueOf(meal.getPrice()));
    }
}
