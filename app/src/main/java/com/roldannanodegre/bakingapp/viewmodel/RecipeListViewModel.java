package com.roldannanodegre.bakingapp.viewmodel;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import com.roldannanodegre.bakingapp.api.RecipesRepository;
import com.roldannanodegre.bakingapp.api.RecipesUrl;
import com.roldannanodegre.bakingapp.sqlite.AppDatabase;
import com.roldannanodegre.bakingapp.sqlite.Ingredient;
import com.roldannanodegre.bakingapp.sqlite.Recipe;
import com.roldannanodegre.bakingapp.sqlite.Step;
import com.roldannanodegre.bakingapp.util.AppExecutors;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class RecipeListViewModel extends AbstractAndroidViewModel implements Callback<List<Recipe>> {

    private LiveData<List<Recipe>> recipesLiveData;
    private LiveData<Recipe> recipeLiveData;

    public RecipeListViewModel(Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        recipesLiveData = database.recipeDao().loadAll();
        RecipesRepository.getBakingApi(RecipesUrl.MAY2017).fetchReceipesMovies().enqueue(this);
    }

    public RecipeListViewModel(Application application, long recipeId) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        recipeLiveData = database.recipeDao().loadRecipe(recipeId);
    }

    @Override
    public void onResponse(@EverythingIsNonNull Call<List<Recipe>> call, @EverythingIsNonNull final Response<List<Recipe>> response) {
        isIdle = true;
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (response.body() != null && !response.body().isEmpty()) {
                    persistRecipes(response.body());
                }
            }
        });
    }

    private void persistRecipes(List<Recipe> recipeList) {
        AppDatabase appDatabase = AppDatabase.getInstance(getApplication());
        appDatabase.recipeDao().deleteAll(recipeList);
        appDatabase.recipeDao().insertAll(recipeList);
        for (Recipe recipe : recipeList) {
            if (recipe.getIngredients() != null && !recipe.getIngredients().isEmpty()) {
                for (Ingredient ingredient : recipe.getIngredients()) {
                    ingredient.setReceipId(recipe.getId());
                }
//                appDatabase.ingredientDao().deleteRecipeIngredients(recipe.getId());
                appDatabase.ingredientDao().insertAll(recipe.getIngredients());
            }
            if (recipe.getSteps() != null && !recipe.getSteps().isEmpty()) {
                for (Step step : recipe.getSteps()) {
                    step.setReceipId(recipe.getId());
                }
                appDatabase.stepDao().deleteAll(recipe.getSteps());
                appDatabase.stepDao().insertAll(recipe.getSteps());
            }
        }
    }

    @Override
    public void onFailure(@EverythingIsNonNull Call<List<Recipe>> call, @EverythingIsNonNull Throwable t) {

    }

    public LiveData<List<Recipe>> getRecipesLiveData() {
        return recipesLiveData;
    }

    public LiveData<Recipe> getRecipeLiveData() {
        return recipeLiveData;
    }

}
