package com.evening.app;

import com.evening.annotation.Factory;

/**
 * Created by Nighter on 17/6/14.
 */

@Factory(
        id = "Calzone",
        type = Meal.class
)
public class CalzonePizza implements Meal {
    @Override
    public float getPrice() {
        return 8.5f;
    }
}
