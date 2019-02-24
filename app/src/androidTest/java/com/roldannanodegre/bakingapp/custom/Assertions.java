package com.roldannanodegre.bakingapp.custom;

import android.support.test.espresso.ViewAssertion;

import com.roldannanodegre.bakingapp.custom.assertion.RecyclerViewItemCountAssertion;

public class Assertions {

    public static ViewAssertion recyclerViewItemCountAssertion(final int expectedCount) {
        return new RecyclerViewItemCountAssertion(expectedCount);
    }

}
