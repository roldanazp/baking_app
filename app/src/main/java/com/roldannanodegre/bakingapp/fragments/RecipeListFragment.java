package com.roldannanodegre.bakingapp.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.roldannanodegre.bakingapp.AbstractActivity;
import com.roldannanodegre.bakingapp.R;
import com.roldannanodegre.bakingapp.StepListActivity;
import com.roldannanodegre.bakingapp.adapters.RecipesAdapter;
import com.roldannanodegre.bakingapp.databinding.FragmentRecipeListBinding;
import com.roldannanodegre.bakingapp.sqlite.Recipe;
import com.roldannanodegre.bakingapp.state.StateProvider;
import com.roldannanodegre.bakingapp.util.DeviceUtils;
import com.roldannanodegre.bakingapp.viewmodel.RecipeListViewModel;

import java.util.List;

public class RecipeListFragment extends AbstractFragment implements RecipesAdapter.RecipeItemListener {

    private RecipesAdapter recipesAdapter;

    public RecipeListFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupViewModel();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return initUi(inflater, container);
    }

    private View initUi(LayoutInflater inflater, ViewGroup container) {
        FragmentRecipeListBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_list, container, false);
        if (getActivity()!=null) {
            getActivity().setActionBar(binding.tbToolbar);
        }
        binding.rvRecipes.setLayoutManager(new GridLayoutManager(getContext(), DeviceUtils.isTablet(getActivity())?3:1));
        recipesAdapter = new RecipesAdapter(this);
        binding.rvRecipes.setAdapter(recipesAdapter);
        return binding.getRoot();
    }

    private void setupViewModel() {
        final RecipeListViewModel viewModel = ViewModelProviders.of(this).get(RecipeListViewModel.class);
        viewModel.getRecipesLiveData().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable final List<Recipe> recipeList) {
                if (viewModel.isIdle){
                    decrement(AbstractActivity.RECIPES_JSON_ID);
                }
                recipesAdapter.replaceAll(recipeList);
            }
        });
    }

    @Override
    public void onRecipeSelected(Recipe recipe, View view) {
        StateProvider.setCurrentRecipeId(getContext(), recipe.getId());
        StateProvider.setCurrentStepId(getContext(), 0);
        view.setTransitionName(String.valueOf(recipe.getId()));
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(getActivity(), view, String.valueOf(recipe.getId()));
        startActivity(new Intent(getContext(), StepListActivity.class));
    }

}
