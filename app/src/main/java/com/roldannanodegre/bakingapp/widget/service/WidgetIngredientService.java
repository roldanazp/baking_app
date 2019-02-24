package com.roldannanodegre.bakingapp.widget.service;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.roldannanodegre.bakingapp.BuildConfig;
import com.roldannanodegre.bakingapp.sqlite.AppDatabase;
import com.roldannanodegre.bakingapp.sqlite.Ingredient;
import com.roldannanodegre.bakingapp.sqlite.Recipe;
import com.roldannanodegre.bakingapp.state.StateProvider;
import com.roldannanodegre.bakingapp.widget.BakingAppWidget;

public class WidgetIngredientService extends IntentService {

    public static final String ACTION_FLIP_INGREDIEMT =
            BuildConfig.APPLICATION_ID + "flip_ingredient";
    private static final String ACTION_FLIP_INGREDIEMT_EXTRA_RECIPE_ID = "recipe_id";
    private static final String ACTION_FLIP_INGREDIEMT_EXTRA_INGREDIENT = "ingredient";

    public WidgetIngredientService() {
        super(WidgetIngredientService.class.getName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FLIP_INGREDIEMT.equals(action)) {
                handleActionFlipIngredient(intent);
            }
        }
    }

    private void handleActionFlipIngredient(Intent intent) {

        long recipeId = intent.getLongExtra(ACTION_FLIP_INGREDIEMT_EXTRA_RECIPE_ID, -1);
        String ingredient = intent.getStringExtra(ACTION_FLIP_INGREDIEMT_EXTRA_INGREDIENT);

        AppDatabase appDatabase = AppDatabase.getInstance(getApplicationContext());
        Ingredient ingredientX = appDatabase.ingredientDao().loadRecipe(recipeId, ingredient);
        ingredientX.setAcquired(!ingredientX.isAcquired());
        appDatabase.ingredientDao().update(ingredientX);

        Recipe recipe = appDatabase.recipeDao().loadRecipeSync(StateProvider.getCurrentWidgetRecipeId(getApplicationContext()));
        BakingAppWidget.updateIngredientsRemoteViews(this, recipe);
    }


    public static PendingIntent getPendingIntentTemplate(Context context){
        Intent intent = new Intent(context, WidgetIngredientService.class);
        intent.setAction(WidgetIngredientService.ACTION_FLIP_INGREDIEMT);
        return PendingIntent.getService(context,
                0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static Intent getFillInIntent(long recipeId, String ingredient){
        Bundle extras = new Bundle();
        extras.putLong(WidgetIngredientService.ACTION_FLIP_INGREDIEMT_EXTRA_RECIPE_ID, recipeId);
        extras.putString(WidgetIngredientService.ACTION_FLIP_INGREDIEMT_EXTRA_INGREDIENT, ingredient);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        return fillInIntent;
    }



}
