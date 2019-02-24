package com.roldannanodegre.bakingapp.adapters;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.roldannanodegre.bakingapp.R;
import com.roldannanodegre.bakingapp.databinding.IngredientListItemBinding;
import com.roldannanodegre.bakingapp.sqlite.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientHolder> {

    private List<Ingredient> ingedientList = new ArrayList<>();
    private final IngredientItemListener stepItemListener;

    public IngredientAdapter(IngredientItemListener stepItemListener) {
        this.stepItemListener = stepItemListener;
    }

    @NonNull
    @Override
    public IngredientHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        IngredientListItemBinding binding = DataBindingUtil.inflate(LayoutInflater
                .from(viewGroup.getContext()), R.layout.ingredient_list_item, viewGroup, false);
        binding.setListener(stepItemListener);
        return new IngredientHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientHolder viewHolder, int index) {
        viewHolder.bind(ingedientList.get(index));
    }

    @Override
    public int getItemCount() {
        return ingedientList.size();
    }

    public void replaceAll(List<Ingredient> ingredientList) {
        this.ingedientList = ingredientList;
        notifyDataSetChanged();
    }

    public void update(List<Ingredient> ingredientList){
        for (Ingredient newIngredient : ingredientList) {
            if (this.ingedientList.contains(newIngredient)) {
                int ingredientIndex = this.ingedientList.indexOf(newIngredient);
                Ingredient oldIngredient = this.ingedientList.get(ingredientIndex);
                if (newIngredient.isAcquired() != oldIngredient.isAcquired()) {
                    oldIngredient.setAcquired(newIngredient.isAcquired());
                    notifyItemChanged(oldIngredient);
                }
            } else {
                this.ingedientList.add(newIngredient);
                notifyItemChanged(this.ingedientList.indexOf(newIngredient));
            }
        }
    }

    public void notifyItemChanged(Ingredient ingredient){
        if (ingedientList.contains(ingredient)) {
            notifyItemChanged(ingedientList.indexOf(ingredient));
        }
    }

    class IngredientHolder extends RecyclerView.ViewHolder {

        final IngredientListItemBinding binding;

        IngredientHolder(IngredientListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Ingredient step) {
            binding.setModel(step);
        }

    }

    public interface IngredientItemListener {
        void onIngredientSelected(Ingredient ingredient);
    }
}
