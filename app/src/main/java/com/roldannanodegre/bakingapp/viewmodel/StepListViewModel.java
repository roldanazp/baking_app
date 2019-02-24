package com.roldannanodegre.bakingapp.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.roldannanodegre.bakingapp.sqlite.AppDatabase;
import com.roldannanodegre.bakingapp.sqlite.Step;

import java.util.List;

public class StepListViewModel extends ViewModel {

    private LiveData<List<Step>> stepsLiveData;

    public StepListViewModel(AppDatabase appDatabase, long recipeId) {
        stepsLiveData = appDatabase.stepDao().loadAllForRecipe(recipeId);
    }

    public LiveData<List<Step>> getStepsLiveData() {
        return stepsLiveData;
    }


}
