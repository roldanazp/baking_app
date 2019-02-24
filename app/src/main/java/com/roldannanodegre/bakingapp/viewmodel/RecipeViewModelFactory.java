package com.roldannanodegre.bakingapp.viewmodel;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class RecipeViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final Application application;
    private final long recipeId;

    public RecipeViewModelFactory(@Nullable Application application, long recipeId) {
        this.application = application;
        this.recipeId = recipeId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new RecipeListViewModel(application, recipeId);
    }

}
