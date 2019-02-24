package com.roldannanodegre.bakingapp.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintSet;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.util.Util;
import com.roldannanodegre.bakingapp.AbstractActivity;
import com.roldannanodegre.bakingapp.R;
import com.roldannanodegre.bakingapp.databinding.FragmentStepDetailBinding;
import com.roldannanodegre.bakingapp.sqlite.AppDatabase;
import com.roldannanodegre.bakingapp.sqlite.Recipe;
import com.roldannanodegre.bakingapp.sqlite.Step;
import com.roldannanodegre.bakingapp.state.StateProvider;
import com.roldannanodegre.bakingapp.viewmodel.RecipeListViewModel;
import com.roldannanodegre.bakingapp.viewmodel.RecipeViewModelFactory;
import com.roldannanodegre.bakingapp.viewmodel.StepDetailViewModel;
import com.roldannanodegre.bakingapp.viewmodel.StepListViewModel;
import com.roldannanodegre.bakingapp.viewmodel.StepViewModelFactory;

import java.util.List;

public class StepDetailFragment extends AbstractFragment {

    private Recipe recipe;
    private Step step;
    private FragmentStepDetailBinding binding;
    private SimpleExoPlayer player;
    private final StepDetailFragment.BakingEventListener bakingEventListener = new StepDetailFragment.BakingEventListener();
    private StepDetailViewModel viewModel;
    private final SharedPreferences.OnSharedPreferenceChangeListener onStateSharedPreferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            long stepId = sharedPreferences.getLong(key, 0);
            int mod = (int) (stepId - step.getId());
            switchStep(mod);
        }
    };

    public StepDetailFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        updateIdlingState(false);
        initUi( inflater, container);
        return binding.getRoot();
    }

    private void initUi(LayoutInflater inflater, ViewGroup container) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_step_detail, container, false);
        binding.setListener(this);
//        updateConstraints();
    }

    private void init(){
        if (getActivity() != null) {
            RecipeViewModelFactory recipeViewModelFactory
                    = new RecipeViewModelFactory(getActivity().getApplication(), StateProvider.getCurrentRecipeId(getContext()));
            final RecipeListViewModel viewModel = ViewModelProviders.of(this, recipeViewModelFactory).get(RecipeListViewModel.class);
            viewModel.getRecipeLiveData().observe(this, new Observer<Recipe>() {
                @Override
                public void onChanged(@Nullable final Recipe recipe) {
                    viewModel.getRecipeLiveData().removeObserver(this);
                    StepDetailFragment.this.recipe = recipe;
                    loadSteps();
                }
            });
        }
    }

    private void loadSteps(){
        AppDatabase appDb = AppDatabase.getInstance(getContext());
        StepViewModelFactory stepViewModelFactory = new StepViewModelFactory(appDb, recipe.getId());
        final StepListViewModel viewModel = ViewModelProviders.of(this, stepViewModelFactory).get(StepListViewModel.class);
        viewModel.getStepsLiveData().observe(this, new Observer<List<Step>>() {
            @Override
            public void onChanged(@Nullable final List<Step> stepList) {
                if (stepList != null) {
                    viewModel.getStepsLiveData().removeObserver(this);
                    StepDetailFragment.this.recipe.setSteps(stepList);
                    step = stepList.get((int) StateProvider.getCurrentStepId(getContext()));
                    binding.setStep(step);
                    initPlayer();
                }
            }
        });
    }


    private void initPlayer(){
        viewModel = ViewModelProviders.of(this).get(StepDetailViewModel.class);
        if (step.getVideoURL() !=null && !step.getVideoURL().isEmpty()) {
            updatePlayerProgressBarConstraints(true);
            player = viewModel.getSimpleExoPlayer(step.getVideoURL());
            player.addListener(bakingEventListener);
            binding.pvPlayer.setPlayer(player);
        }else{
            Log.d("razp", "aplha 1");
            updatePlayerConstraints();
            updatePlayerProgressBarConstraints(false);
        }
    }

    private void clearPlayer() {
        if (player != null) {
            player.stop();
            player = null;
            viewModel = null;
        }
    }

    public void switchStep(int mod){
        if (player!=null) {
            player.stop();
        }
        step = recipe.getSteps().get(step.getId()+mod);
        StateProvider.setCurrentStepId(getContext(), step.getId());
        binding.setStep(step);
        updateConstraints();
        initPlayer();
    }

    private void updateConstraints(){
        ConstraintSet constraintSet = getCurrtentConstraintSet();
        constraintSet.setVisibility(R.id.ib_prev_step, step.getId() == 0? View.INVISIBLE : View.VISIBLE);
        constraintSet.setVisibility(R.id.ib_next_step, step.getId() == recipe.getSteps().size()-1 ? View.INVISIBLE : View.VISIBLE);
        applyConstraintSet(constraintSet);
    }

    private void updatePlayerConstraints(){
        ConstraintSet constraintSet = getCurrtentConstraintSet();
        constraintSet.constrainHeight(R.id.pv_player, step.getVideoURL() !=null && !step.getVideoURL().isEmpty() ? getPlayerHeight() : 1);
        applyConstraintSet(constraintSet);
    }

    private int getPlayerHeight(){
        try{
            if( (((ViewGroup) binding.getRoot().getParent()).getChildCount() == 1)
                    && getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                return ViewGroup.LayoutParams.MATCH_PARENT;
            }else{
                return ViewGroup.LayoutParams.WRAP_CONTENT;
            }
        }catch (Exception exc) {
            //FIXTHIS en las pruebas unitarias
            Log.d("razp", "FIXTHIS");
            return ViewGroup.LayoutParams.MATCH_PARENT;
        }
    }

    private void updatePlayerProgressBarConstraints(boolean isLoading){
        ConstraintSet constraintSet = getCurrtentConstraintSet();
        constraintSet.constrainHeight(R.id.pb_loading, isLoading ? ConstraintSet.WRAP_CONTENT : 0);
        applyConstraintSet(constraintSet);
    }

    private ConstraintSet getCurrtentConstraintSet(){
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(binding.clContainer);
        return constraintSet;
    }

    private void applyConstraintSet(ConstraintSet constraintSet){
        TransitionManager.beginDelayedTransition(binding.clContainer);
        constraintSet.applyTo(binding.clContainer);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getContext() != null) {
            StateProvider.getPreferenceManager(getContext()).registerOnSharedPreferenceChangeListener(onStateSharedPreferenceChangeListener);
        }
        init();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            clearPlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (getContext() != null) {
            StateProvider.getPreferenceManager(getContext()).unregisterOnSharedPreferenceChangeListener(onStateSharedPreferenceChangeListener);
        }
        if (Util.SDK_INT > 23) {
            clearPlayer();
        }
    }

    class BakingEventListener implements Player.EventListener {

        @Override
        public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

        }

        @Override
        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

        }

        @Override
        public void onLoadingChanged(boolean isLoading) {

        }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            if (playbackState == Player.STATE_READY) {
                updatePlayerProgressBarConstraints(false);
                updatePlayerConstraints();
                decrement(AbstractActivity.LOAD_VIDEO_ID);
            }
        }

        @Override
        public void onRepeatModeChanged(int repeatMode) {

        }

        @Override
        public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {

        }

        @Override
        public void onPositionDiscontinuity(int reason) {

        }

        @Override
        public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

        }

        @Override
        public void onSeekProcessed() {

        }
    }


}
