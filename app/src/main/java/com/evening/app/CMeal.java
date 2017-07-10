package com.evening.app;

import com.evening.annotation.Factory;

/**
 * Created by Nighter on 17/6/14.
 */

@Factory(
        id = "C",
        type = Meal.class
)
public class CMeal implements Meal {
    @Override
    public float getPrice() {
        return 4.5f;
    }
}
