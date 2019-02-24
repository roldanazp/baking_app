package com.roldannanodegre.bakingapp.widget.service;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.roldannanodegre.bakingapp.R;
import com.roldannanodegre.bakingapp.sqlite.AppDatabase;
import com.roldannanodegre.bakingapp.sqlite.Ingredient;
import com.roldannanodegre.bakingapp.state.StateProvider;

import java.util.List;

public class RecipeIngredientListRemoteViewsService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new IngredientRemoteViewFactory(getApplicationContext(), StateProvider.getCurrentWidgetRecipeId(getApplicationContext()));
    }
}

class IngredientRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory {

    private final Context applicationContext;
    private long recipeId;
    private List<Ingredient> ingrediente;

    public IngredientRemoteViewFactory(Context applicationContext, long recipeId) {
        this.applicationContext = applicationContext;
        this.recipeId = recipeId;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        AppDatabase appDatabase = AppDatabase.getInstance(applicationContext);
        ingrediente = appDatabase.ingredientDao().loadAllforRecipeSync(recipeId);
    }

    @Override
    public void onDestroy() {
        ingrediente = null;
    }

    @Override
    public int getCount() {
        return ingrediente == null ? 0 : ingrediente.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {

        if (ingrediente == null || ingrediente.isEmpty()) {
            return null;
        }

        Ingredient ingredient = ingrediente.get(position);

        RemoteViews remoteViews = new RemoteViews(applicationContext.getPackageName(), R.layout.widget_ingredient_list_item);

        remoteViews.setTextViewText(R.id.tv_ingredient, ingredient.getIngredient());
        remoteViews.setTextViewText(R.id.tv_quantity_measure, String.valueOf(ingredient.getReceipId()) + " " + ingredient.getMeasure());
        remoteViews.setImageViewResource(R.id.cb_ingredient, ingredient.isAcquired() ? R.drawable.ic_done : R.drawable.ic_checkbox);

        remoteViews.setInt(R.id.ll_ingredient, "setBackgroundColor",
                applicationContext.getColor(ingredient.isAcquired() ?
                        R.color.colorIngredientAcquired : R.color.colorIngredient));

        remoteViews.setOnClickFillInIntent(R.id.ll_ingredient,
                WidgetIngredientService.getFillInIntent(ingredient.getReceipId(), ingredient.getIngredient()));


        return remoteViews;
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