package com.roldannanodegre.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.roldannanodegre.bakingapp.R;
import com.roldannanodegre.bakingapp.widget.service.RecipeIngredientListRemoteViewsService;
import com.roldannanodegre.bakingapp.widget.service.WidgetIngredientService;
import com.roldannanodegre.bakingapp.widget.service.WidgetRecipeListRemoteViewsService;
import com.roldannanodegre.bakingapp.widget.service.WidgetRecipeService;
import com.roldannanodegre.bakingapp.widget.service.WidgetStepService;
import com.roldannanodegre.bakingapp.sqlite.Recipe;

public class BakingAppWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        updateRemoteViews(context);
    }

    public static void updateRemoteViews(Context context){
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, BakingAppWidget.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_grid_view);
        for (int appWidgetId : appWidgetIds) {
            appWidgetManager.updateAppWidget(appWidgetId, getRecipeListRemoteView(context));
        }
    }

    public static void updateIngredientsRemoteViews(Context context, Recipe recipe){
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, BakingAppWidget.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_grid_view);
        for (int appWidgetId : appWidgetIds) {
            appWidgetManager.updateAppWidget(appWidgetId, getIngredientListRemoteView(context, recipe));
        }
    }

    private static RemoteViews getIngredientListRemoteView(Context context, Recipe recipe){
        RemoteViews views = new RemoteViews(context.getPackageName(),
                R.layout.widget_ingredient_list);
        Intent intent = new Intent(context, RecipeIngredientListRemoteViewsService.class);
        views.setRemoteAdapter(R.id.widget_grid_view, intent);

        intent.setAction(WidgetIngredientService.ACTION_FLIP_INGREDIEMT);
        PendingIntent pendingIntent = PendingIntent.getService(context,
                0, WidgetRecipeService.intentActionRecipeList(context), PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.ib_recipe_list, pendingIntent);

        PendingIntent stepActivityPendingIntent = PendingIntent.getService(context,
                0, WidgetStepService.intentActionStepList(context, recipe), PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.ll_container, stepActivityPendingIntent);


        views.setInt(R.id.ll_container, "setBackgroundColor",
            context.getColor(R.color.colorPrimary));

        views.setTextViewText(R.id.tv_ingredient, recipe.getName());
        views.setTextViewText(R.id.tv_quantity_measure, String.valueOf(recipe.getServings()) + " servings");

        views.setPendingIntentTemplate(R.id.widget_grid_view,
                WidgetIngredientService.getPendingIntentTemplate(context));
        views.setEmptyView(R.id.widget_grid_view, R.id.empty_view);
        return views;
    }

    private static RemoteViews getRecipeListRemoteView(Context context){
        RemoteViews views = new RemoteViews(context.getPackageName(),
                R.layout.widget_recipe_list);
        Intent intent = new Intent(context, WidgetRecipeListRemoteViewsService.class);
        views.setRemoteAdapter(R.id.widget_grid_view, intent);
        views.setPendingIntentTemplate(R.id.widget_grid_view,
                WidgetRecipeService.getPendingIntentTemplate(context));
        views.setEmptyView(R.id.widget_grid_view, R.id.empty_view);
        return views;
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
//        WidgetIngredientService.startActionFlipIngredient(context, 1, "salt");
        updateRemoteViews(context);
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // Perform any action when one or more AppWidget instances have been deleted
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

}

