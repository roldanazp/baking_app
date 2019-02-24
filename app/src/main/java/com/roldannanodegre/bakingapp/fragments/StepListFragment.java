package com.roldannanodegre.bakingapp.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.roldannanodegre.bakingapp.IngredientListActivity;
import com.roldannanodegre.bakingapp.R;
import com.roldannanodegre.bakingapp.StepDetailActivity;
import com.roldannanodegre.bakingapp.adapters.StepAdapter;
import com.roldannanodegre.bakingapp.databinding.FragmentStepListBinding;
import com.roldannanodegre.bakingapp.sqlite.AppDatabase;
import com.roldannanodegre.bakingapp.sqlite.Recipe;
import com.roldannanodegre.bakingapp.sqlite.Step;
import com.roldannanodegre.bakingapp.state.StateProvider;
import com.roldannanodegre.bakingapp.util.ImageUtil;
import com.roldannanodegre.bakingapp.viewmodel.RecipeListViewModel;
import com.roldannanodegre.bakingapp.viewmodel.RecipeViewModelFactory;
import com.roldannanodegre.bakingapp.viewmodel.StepListViewModel;
import com.roldannanodegre.bakingapp.viewmodel.StepViewModelFactory;

import java.util.List;

public class StepListFragment extends Fragment implements StepAdapter.StepItemListener {

    private StepAdapter stepAdapter;
    private Recipe recipe;

    private FragmentStepListBinding binding;

    public StepListFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        initUi(inflater, container);
        init();
        return binding.getRoot();
    }

    private void initUi(LayoutInflater inflater, ViewGroup container) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_step_list, container, false);
        binding.setListener(this);
        binding.recipeListItem.getRoot().setBackgroundResource(android.R.color.white);
        binding.recipeListItem.getRoot().setSoundEffectsEnabled(false);
        binding.rvSteps.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        stepAdapter = new StepAdapter(this);
        binding.rvSteps.setAdapter(stepAdapter);
    }

    private void init(){
        if (getActivity() != null) {
            RecipeViewModelFactory recipeViewModelFactory
                    = new RecipeViewModelFactory(getActivity().getApplication(), StateProvider.getCurrentRecipeId(getContext()));
            final RecipeListViewModel viewModel = ViewModelProviders.of(this, recipeViewModelFactory).get(RecipeListViewModel.class);
            viewModel.getRecipeLiveData().observe(this, new Observer<Recipe>() {
                @Override
                public void onChanged(@Nullable final Recipe recipe) {
                    if (recipe!=null) {
                        viewModel.getRecipeLiveData().removeObserver(this);
                        StepListFragment.this.recipe = recipe;
                        binding.recipeListItem.setModel(recipe);
                        binding.recipeListItem.getRoot().setTransitionName(String.valueOf(recipe.getId()));
                        ImageUtil.setImage(binding.recipeListItem.ivRecipeImage, recipe.getImage());
                        setupViewModel();
                    }
                }
            });
        }
    }

    private void setupViewModel() {
        AppDatabase appDb = AppDatabase.getInstance(getContext());
        StepViewModelFactory stepViewModelFactory = new StepViewModelFactory(appDb, recipe.getId());
        final StepListViewModel viewModel = ViewModelProviders.of(this, stepViewModelFactory).get(StepListViewModel.class);
        viewModel.getStepsLiveData().observe(this, new Observer<List<Step>>() {
            @Override
            public void onChanged(@Nullable final List<Step> stepList) {
                viewModel.getStepsLiveData().removeObserver(this);
                stepAdapter.replaceAll(stepList);
                recipe.setSteps(stepList);
            }
        });
    }

    public void onIngredientesSelected(){
        startActivity(new Intent(getContext(), IngredientListActivity.class));
    }

    @Override
    public void onStepSelected(Step step) {
        StateProvider.setCurrentStepId(getContext(), step.getId());
        if (((ViewGroup) binding.getRoot().getParent()).getChildCount() == 1) {
            startActivity(new Intent(getContext(), StepDetailActivity.class));
        }
    }

}
