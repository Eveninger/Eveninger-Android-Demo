package com.evening.app;

import com.evening.annotation.Factory;

/**
 * Created by Nighter on 17/6/14.
 */

@Factory(
        id = "Margherita",
        type = Meal.class
)
public class MargheritaPizza implements Meal {
    @Override
    public float getPrice() {
        return 6.0f;
    }
}
