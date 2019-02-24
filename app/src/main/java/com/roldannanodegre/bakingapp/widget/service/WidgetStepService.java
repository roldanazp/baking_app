package com.roldannanodegre.bakingapp.widget.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.roldannanodegre.bakingapp.BuildConfig;
import com.roldannanodegre.bakingapp.StepListActivity;
import com.roldannanodegre.bakingapp.sqlite.Recipe;
import com.roldannanodegre.bakingapp.state.StateProvider;

public class WidgetStepService extends IntentService {

    private static final String ACTION_OPEN_STEP_LIST =
            BuildConfig.APPLICATION_ID + "action_open_step_list";
    private static final String ACTION_OPEN_STEP_EXTRA_RECIPE_ID = "recipe_id";


    public WidgetStepService() {
        super(WidgetStepService.class.getName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_OPEN_STEP_LIST.equals(action)) {
                handleActionSelectStepList(intent);
            }
        }
    }

    private void handleActionSelectStepList(Intent intent) {

        long recipeId = intent.getLongExtra(ACTION_OPEN_STEP_EXTRA_RECIPE_ID, -1);
        if (recipeId != -1) {
            StateProvider.setCurrentRecipeId(getApplicationContext(), recipeId);
            Intent stepListIntent = new Intent(getApplicationContext(), StepListActivity.class);
            stepListIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            stepListIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(stepListIntent);
        }

    }

    public static Intent intentActionStepList(Context context, Recipe recipe) {
        Intent intent = new Intent(context, WidgetStepService.class);
        intent.putExtra(ACTION_OPEN_STEP_EXTRA_RECIPE_ID, recipe.getId());
        intent.setAction(ACTION_OPEN_STEP_LIST);
        return intent;
    }

}
