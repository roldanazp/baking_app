package com.roldannanodegre.bakingapp.state;

import android.content.Context;
import android.content.SharedPreferences;

public class StateProvider {

    private static final String BAKING_PREFERENCES_NAME = "baking_preferences_name";
    private static final String CURRENT_RECIPE_ID_KEY = "current_recipe_id_key";
    private static final String CURRENT_WIDGET_RECIPE_ID_KEY = "current_widget_recipe_id_key";
    private static final String CURRENT_STEP_ID_KEY = "current_step_id_key";

    public static long getCurrentRecipeId(Context context){
        return getPreferenceManager(context).getLong(CURRENT_RECIPE_ID_KEY, 0);
    }

    public static void setCurrentRecipeId(Context context, long recipeId){
        getPreferenceManager(context).edit().putLong(CURRENT_RECIPE_ID_KEY, recipeId).apply();
    }

    public static long getCurrentStepId(Context context){
        return getPreferenceManager(context).getLong(CURRENT_STEP_ID_KEY, 0);
    }

    public static void setCurrentStepId(Context context, long stepId){
        getPreferenceManager(context).edit().putLong(CURRENT_STEP_ID_KEY, stepId).apply();
    }

    public static SharedPreferences getPreferenceManager(Context context){
        return context.getSharedPreferences(BAKING_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public static long getCurrentWidgetRecipeId(Context context){
        return getPreferenceManager(context).getLong(CURRENT_WIDGET_RECIPE_ID_KEY, 0);
    }

    public static void setCurrentWidgetRecipeId(Context context, long recipeId){
        getPreferenceManager(context).edit().putLong(CURRENT_WIDGET_RECIPE_ID_KEY, recipeId).apply();
    }

}
