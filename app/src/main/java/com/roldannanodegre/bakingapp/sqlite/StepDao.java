package com.roldannanodegre.bakingapp.sqlite;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface StepDao {

    @Query("SELECT * FROM step")
    LiveData<List<Step>> loadAll();

    @Query("SELECT * FROM step WHERE receipId = :recipeId")
    LiveData<List<Step>> loadAllForRecipe(long recipeId);

    @Query("SELECT * FROM step WHERE receipId = :recipeId")
    List<Step> loadAllForRecipeAsync(long recipeId);

    @Query("SELECT * FROM step WHERE receipId = :recipeId AND id = :stepId")
    Step loadStepAsync(long recipeId, long stepId);

    @Insert
    void insert(Step step);

    @Insert
    void insertAll(List<Step> stepList);

    @Delete
    void delete(Step step);

    @Delete
    void deleteAll(List<Step> stepList);

}
