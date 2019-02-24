package com.roldannanodegre.bakingapp.adapters;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.roldannanodegre.bakingapp.R;
import com.roldannanodegre.bakingapp.databinding.RecipeListItemBinding;
import com.roldannanodegre.bakingapp.sqlite.Recipe;
import com.roldannanodegre.bakingapp.util.ImageUtil;

import java.util.ArrayList;
import java.util.List;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipeHolder> {

    private List<Recipe> recipeList = new ArrayList<>();
    private final RecipeItemListener recipeItemListener;

    public RecipesAdapter(RecipeItemListener recipeItemListener) {
        this.recipeItemListener = recipeItemListener;
    }

    @NonNull
    @Override
    public RecipesAdapter.RecipeHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        RecipeListItemBinding binding = DataBindingUtil.inflate(LayoutInflater
                .from(viewGroup.getContext()), R.layout.recipe_list_item, viewGroup, false);
        binding.setListener(recipeItemListener);
        binding.setSelf(binding);
        return new RecipeHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeHolder viewHolder, int index) {
        viewHolder.bind(recipeList.get(index));
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public void replaceAll(List<Recipe> recipeList) {
        this.recipeList = recipeList;
        notifyDataSetChanged();
    }

    class RecipeHolder extends RecyclerView.ViewHolder {

        final RecipeListItemBinding binding;

        RecipeHolder(RecipeListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Recipe recipe) {
            binding.setModel(recipe);
            ImageUtil.setImage(binding.ivRecipeImage, recipe.getImage());
        }

    }

    public interface RecipeItemListener {
        void onRecipeSelected(Recipe recipe, View view);
    }
}
