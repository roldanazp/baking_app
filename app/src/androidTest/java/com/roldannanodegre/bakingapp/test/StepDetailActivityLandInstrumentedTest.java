package com.roldannanodegre.bakingapp.test;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.Display;

import com.roldannanodegre.bakingapp.R;
import com.roldannanodegre.bakingapp.StepDetailActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.roldannanodegre.bakingapp.custom.Matchers.fullScreen;


@RunWith(AndroidJUnit4.class)
public class StepDetailActivityLandInstrumentedTest {

    @Rule
    public final ActivityTestRule<StepDetailActivity> mActivityTestRule =
            new ActivityTestRule<>(StepDetailActivity.class);
    private IdlingResource mIdlingResource;

    @Before
    public void before(){
        mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register(mIdlingResource);
    }

    @After
    public void after(){
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
    }

    @Test
    public void videoFullScreenTest() {
        onView(withId(R.id.headlines_fragment)).check(matches(isDisplayed()));
        Display display = mActivityTestRule.getActivity().getWindowManager().getDefaultDisplay();
        onView(withId(R.id.pv_player)).check(matches(fullScreen(display)));
    }


}
