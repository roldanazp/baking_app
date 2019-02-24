package com.roldannanodegre.bakingapp.sqlite;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface IngredientDao {

    @Query("SELECT * FROM ingredients")
    LiveData<List<Ingredient>> loadAllAsync();

    @Query("SELECT * FROM ingredients WHERE receipId = :receipId")
    List<Ingredient> loadAllforRecipeSync(long receipId);

    @Query("SELECT * FROM ingredients WHERE receipId = :recipeIds")
    LiveData<List<Ingredient>> loadAllforRecipe(long recipeIds);

    @Query("SELECT * FROM ingredients WHERE receipId = :recipeIds AND ingredient = :ingredient")
    Ingredient loadRecipe(long recipeIds, String ingredient);

    @Insert
    void insert(Ingredient ingredient);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(List<Ingredient> ingredientList);

    @Delete
    void delete(Ingredient ingredient);

    @Update
    void update(Ingredient ingredient);

    @Query("DELETE FROM ingredients WHERE receipId = :recipeIds")
    void deleteRecipeIngredients(long recipeIds);


}
