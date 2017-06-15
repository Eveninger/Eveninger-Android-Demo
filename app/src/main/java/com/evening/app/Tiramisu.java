package com.evening.app;

import com.evening.annotation.Factory;

/**
 * Created by Nighter on 17/6/14.
 */

@Factory(
        id = "Tiramisu",
        type = Meal.class
)
public class Tiramisu implements Meal {
    @Override
    public float getPrice() {
        return 4.5f;
    }
}
