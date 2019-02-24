package com.roldannanodegre.bakingapp.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.roldannanodegre.bakingapp.sqlite.AppDatabase;
import com.roldannanodegre.bakingapp.sqlite.Ingredient;

import java.util.List;

public class IngredientListViewModel extends ViewModel {

    private LiveData<List<Ingredient>> ingredientsLiveData;

    public IngredientListViewModel(AppDatabase appDatabase, long recipeId) {
        ingredientsLiveData = appDatabase.ingredientDao().loadAllforRecipe(recipeId);
    }

    public LiveData<List<Ingredient>> getIngredientsLiveData() {
        return ingredientsLiveData;
    }

}
