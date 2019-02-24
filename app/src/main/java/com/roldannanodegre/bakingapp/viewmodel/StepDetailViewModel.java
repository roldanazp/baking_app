package com.roldannanodegre.bakingapp.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class StepDetailViewModel extends AndroidViewModel {

    private final SimpleExoPlayer simpleExoPlayer;
    private int currentWindow;
    private long playbackPosition;
    private String url;

    public StepDetailViewModel(@NonNull Application application) {
        super(application);
        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(application, new DefaultTrackSelector());
        simpleExoPlayer.setPlayWhenReady(true);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        simpleExoPlayer.release();
    }

    public SimpleExoPlayer getSimpleExoPlayer(String url) {

        persistProgress(url);
        simpleExoPlayer.prepare(buildMediaSource(url));
        simpleExoPlayer.seekTo(currentWindow, playbackPosition);

        return simpleExoPlayer;

    }

    private void persistProgress(String url){
        boolean resume = this.url != null && this.url.equals(url);
        currentWindow = resume ? simpleExoPlayer.getCurrentWindowIndex() : 0;
        playbackPosition = resume ? simpleExoPlayer.getCurrentPosition()-100 : 0;
        this.url = url;
    }

    private ExtractorMediaSource buildMediaSource(String url) {

        DefaultDataSourceFactory dataSourceFactory =
                new DefaultDataSourceFactory(
                        getApplication().getApplicationContext(),
                        Util.getUserAgent(getApplication().getApplicationContext(), "exo_demo"));

        return new ExtractorMediaSource.Factory(dataSourceFactory).
                createMediaSource(Uri.parse(url));

    }

}
