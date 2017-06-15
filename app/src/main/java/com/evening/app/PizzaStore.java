package com.evening.app;

/**
 * Created by Nighter on 17/6/14.
 */

public class PizzaStore {
    private MealFactory mMealFactory = new MealFactory();

    public Meal order(String mealName) {
        return mMealFactory.create(mealName);
    }
}
