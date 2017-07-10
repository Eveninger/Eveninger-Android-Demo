package com.evening.app;

import com.evening.annotation.Factory;

/**
 * Created by Nighter on 17/6/14.
 */

@Factory(
        id = "B",
        type = Meal.class
)
public class BMeal implements Meal {
    @Override
    public float getPrice() {
        return 6.0f;
    }
}
