package com.roldannanodegre.bakingapp.adapters;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.roldannanodegre.bakingapp.R;
import com.roldannanodegre.bakingapp.databinding.StepListItemBinding;
import com.roldannanodegre.bakingapp.sqlite.Step;
import com.roldannanodegre.bakingapp.util.ImageUtil;

import java.util.ArrayList;
import java.util.List;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepHolder> {

    private List<Step> stepList = new ArrayList<>();
    private final StepItemListener stepItemListener;

    public StepAdapter(StepItemListener stepItemListener) {
        this.stepItemListener = stepItemListener;
    }

    @NonNull
    @Override
    public StepHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        StepListItemBinding binding = DataBindingUtil.inflate(LayoutInflater
                .from(viewGroup.getContext()), R.layout.step_list_item, viewGroup, false);
        binding.setListener(stepItemListener);
        return new StepHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StepHolder viewHolder, int index) {
        viewHolder.bind(stepList.get(index));
    }

    @Override
    public int getItemCount() {
        return stepList.size();
    }

    public void replaceAll(List<Step> stepList) {
        this.stepList = stepList;
        notifyDataSetChanged();
    }

    class StepHolder extends RecyclerView.ViewHolder {

        final StepListItemBinding binding;

        StepHolder(StepListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Step step) {
            binding.setModel(step);
            boolean hasThumbnail = !(step.getThumbnailURL() == null || step.getThumbnailURL().isEmpty());
            binding.ivStepImage.getRoot().setVisibility( hasThumbnail ? View.VISIBLE : View.GONE );
            if (hasThumbnail) {
                ImageUtil.setImage(binding.ivStepImage, step.getThumbnailURL());
            }
        }

    }

    public interface StepItemListener {
        void onStepSelected(Step step);
    }
}
