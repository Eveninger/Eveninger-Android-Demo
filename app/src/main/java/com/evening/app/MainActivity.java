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

        PizzaStore pizzaStore = new PizzaStore();
        Meal meal = pizzaStore.order("Calzone");
        priceTV.setText(String.valueOf(meal.getPrice()));
    }
}
