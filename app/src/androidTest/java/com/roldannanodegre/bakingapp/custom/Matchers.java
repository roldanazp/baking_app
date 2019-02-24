package com.roldannanodegre.bakingapp.custom;

import android.view.Display;
import android.view.View;

import com.roldannanodegre.bakingapp.custom.matcher.FullScreenMatcher;

import org.hamcrest.Matcher;

public class Matchers {

    public static Matcher<View> fullScreen(final Display display) {
        return new FullScreenMatcher(display, View.class);
    }
}
