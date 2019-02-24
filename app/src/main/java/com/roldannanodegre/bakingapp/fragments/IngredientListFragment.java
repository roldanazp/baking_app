package com.roldannanodegre.bakingapp.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.roldannanodegre.bakingapp.R;
import com.roldannanodegre.bakingapp.adapters.IngredientAdapter;
import com.roldannanodegre.bakingapp.databinding.FragmentIngredientListBinding;
import com.roldannanodegre.bakingapp.sqlite.AppDatabase;
import com.roldannanodegre.bakingapp.sqlite.Ingredient;
import com.roldannanodegre.bakingapp.sqlite.Recipe;
import com.roldannanodegre.bakingapp.state.StateProvider;
import com.roldannanodegre.bakingapp.util.AppExecutors;
import com.roldannanodegre.bakingapp.util.DeviceUtils;
import com.roldannanodegre.bakingapp.viewmodel.IngredientListViewModel;
import com.roldannanodegre.bakingapp.viewmodel.IngredientViewModelFactory;
import com.roldannanodegre.bakingapp.viewmodel.RecipeListViewModel;
import com.roldannanodegre.bakingapp.viewmodel.RecipeViewModelFactory;
import com.roldannanodegre.bakingapp.widget.BakingAppWidget;

import java.util.List;

public class IngredientListFragment extends Fragment implements IngredientAdapter.IngredientItemListener {

    private Recipe recipe;
    private IngredientAdapter ingredientAdapter;
    private AppDatabase appDatabase;
    private FragmentIngredientListBinding binding;

    public IngredientListFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appDatabase = AppDatabase.getInstance(getActivity());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        initUi(inflater, container);
        init();
        return binding.getRoot();
    }

    private void initUi(LayoutInflater inflater, ViewGroup container) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_ingredient_list, container, false);
        ingredientAdapter = new IngredientAdapter(this);
        binding.rvIngredients.setLayoutManager(new GridLayoutManager(getContext(), DeviceUtils.isTablet(getActivity())?3:2));
        binding.rvIngredients.setAdapter(ingredientAdapter);
    }

    private void init(){
        RecipeViewModelFactory recipeViewModelFactory
                = new RecipeViewModelFactory(getActivity().getApplication(), StateProvider.getCurrentRecipeId(getContext()));
        final RecipeListViewModel viewModel = ViewModelProviders.of(this, recipeViewModelFactory).get(RecipeListViewModel.class);
        viewModel.getRecipeLiveData().observe(this, new Observer<Recipe>() {
            @Override
            public void onChanged(@Nullable final Recipe recipe) {
                IngredientListFragment.this.recipe = recipe;
                viewModel.getRecipeLiveData().removeObserver(this);
                setupViewModel();
            }
        });
    }

    private void setupViewModel() {
        IngredientViewModelFactory ingredientViewModelFactory = new IngredientViewModelFactory(appDatabase, recipe.getId());
        final IngredientListViewModel viewModel = ViewModelProviders.of(this, ingredientViewModelFactory).get(IngredientListViewModel.class);
        viewModel.getIngredientsLiveData().observe(this, new Observer<List<Ingredient>>() {
            @Override
            public void onChanged(@Nullable final List<Ingredient> stepList) {
//                viewModel.getIngredientsLiveData().removeObserver(this);
                ingredientAdapter.update(stepList);
            }
        });
    }

    @Override
    public void onIngredientSelected(final Ingredient ingredient) {
        ingredient.setAcquired(!ingredient.isAcquired());
        ingredientAdapter.notifyItemChanged(ingredient);
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                appDatabase.ingredientDao().update(ingredient);
            }
        });
        BakingAppWidget.updateIngredientsRemoteViews(getContext(), recipe);
    }

}
