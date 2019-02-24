package com.roldannanodegre.bakingapp.test;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.roldannanodegre.bakingapp.R;
import com.roldannanodegre.bakingapp.StepDetailActivity;
import com.roldannanodegre.bakingapp.sqlite.AppDatabase;
import com.roldannanodegre.bakingapp.sqlite.Step;
import com.roldannanodegre.bakingapp.state.StateProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


@RunWith(AndroidJUnit4.class)
public class StepDetailActivityInstrumentedTest {

    @Rule
    public ActivityTestRule<StepDetailActivity> mActivityTestRule =
            new ActivityTestRule<>(StepDetailActivity.class);

    @Before
    public void before(){
        Intents.init();
    }

    @After
    public void after(){
        Intents.release();
    }

    //TODO test for video loading

    @Test
    public void switchStepTest() {

        Context appContext = InstrumentationRegistry.getTargetContext();

        AppDatabase appDatabase = AppDatabase.getInstance(appContext);

        long recipeId = StateProvider.getCurrentRecipeId(appContext);
        long stepId = StateProvider.getCurrentStepId(appContext);

        List<Step> stepList = appDatabase.stepDao().loadAllForRecipeAsync(recipeId);
        int buttonId = stepId < stepList.size()-1 ? R.id.ib_next_step:R.id.ib_prev_step;

        onView(withId(buttonId)).perform(click());

        long nextStepId = StateProvider.getCurrentStepId(appContext);

        Step step = appDatabase.stepDao().loadStepAsync(recipeId, nextStepId);

        onView(withId(R.id.tv_step_description)).check(matches(withText(step.getDescription())));

        StateProvider.setCurrentStepId(appContext, 0);

    }


}
