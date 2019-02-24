package com.roldannanodegre.bakingapp.widget.service;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.roldannanodegre.bakingapp.BuildConfig;
import com.roldannanodegre.bakingapp.sqlite.AppDatabase;
import com.roldannanodegre.bakingapp.sqlite.Recipe;
import com.roldannanodegre.bakingapp.state.StateProvider;
import com.roldannanodegre.bakingapp.widget.BakingAppWidget;

public class WidgetRecipeService extends IntentService {

    private static final String ACTION_SELECT_RECIPE =
            BuildConfig.APPLICATION_ID + "action_select_recipe";
    private static final String ACTION_SELECT_RECIPE_LIST =
            BuildConfig.APPLICATION_ID + "action_select_recipe_list";
    private static final String ACTION_SELECT_RECIPE_EXTRA_RECIPE_ID = "recipe_id";

    public WidgetRecipeService() {
        super(WidgetRecipeService.class.getName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_SELECT_RECIPE.equals(action)) {
                handleActionSelectRecipe(intent);
            } else if (ACTION_SELECT_RECIPE_LIST.equals(action)) {
                handleActionSelectRecipeList();
            }
        }
    }

    private void handleActionSelectRecipeList() {
        StateProvider.setCurrentWidgetRecipeId(getApplicationContext(), -1);
        BakingAppWidget.updateRemoteViews(this);
    }

    private void handleActionSelectRecipe(Intent intent) {

        long recipeId = intent.getLongExtra(ACTION_SELECT_RECIPE_EXTRA_RECIPE_ID, -1);
        StateProvider.setCurrentWidgetRecipeId(getApplicationContext(), recipeId);

        AppDatabase appDatabase = AppDatabase.getInstance(getApplicationContext());
        Recipe recipe = appDatabase.recipeDao().loadRecipeSync(StateProvider.getCurrentWidgetRecipeId(getApplicationContext()));

        BakingAppWidget.updateIngredientsRemoteViews(this, recipe);

    }

    public static PendingIntent getPendingIntentTemplate(Context context){
        Intent intent = new Intent(context, WidgetRecipeService.class);
        intent.setAction(WidgetRecipeService.ACTION_SELECT_RECIPE);
        return PendingIntent.getService(context,
                0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static Intent getFillInIntent(long recipeId){
        Bundle extras = new Bundle();
        extras.putLong(WidgetRecipeService.ACTION_SELECT_RECIPE_EXTRA_RECIPE_ID, recipeId);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        return fillInIntent;
    }

    public static Intent intentActionRecipeList(Context context) {
        Intent intent = new Intent(context, WidgetRecipeService.class);
        intent.setAction(WidgetRecipeService.ACTION_SELECT_RECIPE_LIST);
        return intent;
    }


}
