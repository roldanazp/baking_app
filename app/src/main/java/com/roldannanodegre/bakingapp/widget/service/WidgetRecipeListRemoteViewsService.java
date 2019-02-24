package com.roldannanodegre.bakingapp.widget.service;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.roldannanodegre.bakingapp.R;
import com.roldannanodegre.bakingapp.sqlite.AppDatabase;
import com.roldannanodegre.bakingapp.sqlite.Recipe;

import java.util.List;

public class WidgetRecipeListRemoteViewsService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RecipeRemoteViewFactory(getApplicationContext());
    }
}

class RecipeRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory {

    private final Context applicationContext;
    private List<Recipe> recipes;

    public RecipeRemoteViewFactory(Context applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        AppDatabase appDatabase = AppDatabase.getInstance(applicationContext);
        recipes = appDatabase.recipeDao().loadAllSync();
    }

    @Override
    public void onDestroy() {
        recipes = null;
    }

    @Override
    public int getCount() {
        return recipes == null ? 0 : recipes.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {

        if (recipes == null || recipes.isEmpty()) {
            return null;
        }

        Recipe recipe = recipes.get(position);

        RemoteViews views = new RemoteViews(applicationContext.getPackageName(), R.layout.widget_recipe_list_item);

        views.setTextViewText(R.id.tv_ingredient, recipe.getName());
        views.setTextViewText(R.id.tv_quantity_measure, String.valueOf(recipe.getServings()) + " servings");

        views.setOnClickFillInIntent(R.id.ll_ingredient,
                WidgetRecipeService.getFillInIntent(recipe.getId()));


        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

}