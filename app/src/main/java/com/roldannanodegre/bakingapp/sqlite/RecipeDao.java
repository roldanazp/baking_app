package com.roldannanodegre.bakingapp.sqlite;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface RecipeDao {

    @Query("SELECT * FROM recipes")
    LiveData<List<Recipe>> loadAll();

    @Query("SELECT * FROM recipes")
    List<Recipe> loadAllSync();

    @Query("SELECT * FROM recipes WHERE id = :id")
    LiveData<Recipe> loadRecipe(long id);

    @Query("SELECT * FROM recipes WHERE id = :id")
    Recipe loadRecipeSync(long id);

    @Insert
    void insert(Recipe recipe);

    @Insert
    void insertAll(List<Recipe> recipeList);

    @Delete
    void delete(Recipe recipe);

    @Delete
    void deleteAll(List<Recipe> recipeList);


}
