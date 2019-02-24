package com.roldannanodegre.bakingapp.custom.matcher;

import android.graphics.Point;
import android.support.test.espresso.intent.Checks;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.view.Display;
import android.view.View;

import org.hamcrest.Description;

public class FullScreenMatcher extends BoundedMatcher<View, View> {

    private final Display display;

    public FullScreenMatcher(Display display, Class expectedType) {
        super(expectedType);
        Checks.checkNotNull(display);
        this.display = display;
    }

    @Override
    public boolean matchesSafely(View view) {
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        return view.getWidth() == width && view.getHeight() == height;
    }
    @Override
    public void describeTo(Description description) {
        description.appendText("full screen: ");
    }

}
