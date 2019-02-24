package com.roldannanodegre.bakingapp.test;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.roldannanodegre.bakingapp.R;
import com.roldannanodegre.bakingapp.RecipeListActivity;
import com.roldannanodegre.bakingapp.StepListActivity;
import com.roldannanodegre.bakingapp.sqlite.AppDatabase;
import com.roldannanodegre.bakingapp.sqlite.Recipe;
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
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.roldannanodegre.bakingapp.custom.Assertions.recyclerViewItemCountAssertion;
import static com.roldannanodegre.bakingapp.custom.Util.pickRandomItem;


@RunWith(AndroidJUnit4.class)
public class RecipeListActivityInstrumentedTest {

    @Rule
    public final ActivityTestRule<RecipeListActivity> mActivityTestRule =
            new ActivityTestRule<>(RecipeListActivity.class);
    private IdlingResource mIdlingResource;

    @Before
    public void before(){
        Intents.init();
        mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register(mIdlingResource);
    }

    @After
    public void after(){
        Intents.release();
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
    }

    @Test
    public void useAppContext() {

        Context appContext = InstrumentationRegistry.getTargetContext();


        onView(withId(R.id.rv_recipes)).check(matches(isDisplayed()));


        List<Recipe> recipeList = AppDatabase.getInstance(appContext).recipeDao().loadAllSync();
        int randomItem = pickRandomItem(0, recipeList.size());

        StateProvider.setCurrentStepId(appContext, 0);

        onView(ViewMatchers.withId(R.id.rv_recipes))
                .check(recyclerViewItemCountAssertion(recipeList.size()));

        onView(ViewMatchers.withId(R.id.rv_recipes))
                .perform(RecyclerViewActions.scrollToPosition(randomItem));

        onView(withText(recipeList.get(randomItem).getName())).check(matches(isDisplayed()));

        onView(ViewMatchers.withId(R.id.rv_recipes))
                .perform(RecyclerViewActions.actionOnItemAtPosition(randomItem, click()));

        intended(hasComponent(StepListActivity.class.getName()));

    }


}
