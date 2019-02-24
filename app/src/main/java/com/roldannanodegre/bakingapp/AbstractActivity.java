package com.roldannanodegre.bakingapp;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.v7.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractActivity extends AppCompatActivity {

    public static final String RECIPES_JSON_ID = "recipes_json_id";
    public static final String LOAD_VIDEO_ID = "load_video_id";

    @Nullable
    private final CountingIdlingResource countingIdlingResource = new CountingIdlingResource("fuck_espresso");

    private final Map<String, Boolean> changes = new HashMap<>();

    @VisibleForTesting
    public IdlingResource getIdlingResource() {
        return countingIdlingResource;
    }

    public void increment(String id){
        if (countingIdlingResource != null) {
            changes.put(id, true);
            countingIdlingResource.increment();
        }
    }

    public void decrement(String id){
        if (countingIdlingResource != null && changes.containsKey(id) && changes.get(id)) {
            changes.put(id, false);
            countingIdlingResource.decrement();
        }
    }

}
