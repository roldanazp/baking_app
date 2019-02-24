package com.roldannanodegre.bakingapp.test;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.roldannanodegre.bakingapp.IngredientListActivity;
import com.roldannanodegre.bakingapp.R;
import com.roldannanodegre.bakingapp.StepDetailActivity;
import com.roldannanodegre.bakingapp.StepListActivity;
import com.roldannanodegre.bakingapp.sqlite.AppDatabase;
import com.roldannanodegre.bakingapp.sqlite.Recipe;
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
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.roldannanodegre.bakingapp.custom.Assertions.recyclerViewItemCountAssertion;
import static org.hamcrest.CoreMatchers.containsString;


@RunWith(AndroidJUnit4.class)
public class StepListActivityInstrumentedTest {

    @Rule
    public ActivityTestRule<StepListActivity> mActivityTestRule =
            new ActivityTestRule<>(StepListActivity.class);

    @Before
    public void before(){
        Intents.init();
    }

    @After
    public void after(){
        Intents.release();
    }

    @Test
    public void recipeTest() {

        Context appContext = InstrumentationRegistry.getTargetContext();

        long recipeId = StateProvider.getCurrentRecipeId(appContext);

        Recipe recipe = AppDatabase.getInstance(appContext)
                .recipeDao().loadRecipeSync(recipeId);

        onView(ViewMatchers.withId(R.id.tv_name))
                .check(matches(withText(containsString(recipe.getName()))));

    }

    @Test
    public void stepListTest() {

        Context appContext = InstrumentationRegistry.getTargetContext();
        long recipeId = StateProvider.getCurrentRecipeId(appContext);


        List<Step> stepList = AppDatabase.getInstance(appContext)
                .stepDao().loadAllForRecipeAsync(recipeId);

        onView(ViewMatchers.withId(R.id.rv_steps))
                .check(recyclerViewItemCountAssertion(stepList.size()));

        onView(ViewMatchers.withId(R.id.rv_steps))
                .perform(RecyclerViewActions.scrollToPosition(0));

        onView(withText(stepList.get(0).getShortDescription())).check(matches(isDisplayed()));

        onView(ViewMatchers.withId(R.id.rv_steps))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        intended(hasComponent(StepDetailActivity.class.getName()));

        StateProvider.setCurrentStepId(appContext, 0);

    }

    @Test
    public void ingredientsTest() {

        onView(ViewMatchers.withId(R.id.b_ingredients))
                .perform(click());

        intended(hasComponent(IngredientListActivity.class.getName()));

    }

}
