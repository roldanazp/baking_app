package com.roldannanodegre.bakingapp.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.roldannanodegre.bakingapp.sqlite.AppDatabase;

public class IngredientViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final AppDatabase appDb;
    private final long recipeId;

    public IngredientViewModelFactory(AppDatabase appDb, long recipeId) {
        this.appDb = appDb;
        this.recipeId = recipeId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new IngredientListViewModel(appDb, recipeId);
    }
}
