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
import com.roldannanodegre.bakingapp.sqlite.AppDatabase;
import com.roldannanodegre.bakingapp.sqlite.Ingredient;
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
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.roldannanodegre.bakingapp.custom.Assertions.recyclerViewItemCountAssertion;
import static com.roldannanodegre.bakingapp.custom.Util.pickRandomItem;
import static org.junit.Assert.assertNotEquals;


@RunWith(AndroidJUnit4.class)
public class IngredientListActivityInstrumentedTest {

    @Rule
    public ActivityTestRule<IngredientListActivity> mActivityTestRule =
            new ActivityTestRule<>(IngredientListActivity.class);

    @Before
    public void before(){
        Intents.init();
    }

    @After
    public void after(){
        Intents.release();
    }

    @Test
    public void ingredientListTest() {

        Context appContext = InstrumentationRegistry.getTargetContext();
        long recipeId = StateProvider.getCurrentRecipeId(appContext);


        List<Ingredient> ingredientList = AppDatabase.getInstance(appContext)
                .ingredientDao().loadAllforRecipeSync(recipeId);


        String ingredientDescription = ingredientList.get(0).getIngredient();
        boolean isAcquired = ingredientList.get(0).isAcquired();

        onView(ViewMatchers.withId(R.id.rv_ingredients))
                .check(recyclerViewItemCountAssertion(ingredientList.size()));

        onView(ViewMatchers.withId(R.id.rv_ingredients))
                .perform(RecyclerViewActions.scrollToPosition(0));

        onView(withText(ingredientList.get(0).getIngredient())).check(matches(isDisplayed()));

        onView(ViewMatchers.withId(R.id.rv_ingredients))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));


        Ingredient ingredient = AppDatabase.getInstance(appContext)
                .ingredientDao().loadRecipe(recipeId, ingredientDescription);

        assertNotEquals(isAcquired, ingredient.isAcquired());

    }

    @Test
    public void ingredientListItemCLickTest() {

        Context appContext = InstrumentationRegistry.getTargetContext();
        long recipeId = StateProvider.getCurrentRecipeId(appContext);

        List<Ingredient> ingredientList = AppDatabase.getInstance(appContext)
                .ingredientDao().loadAllforRecipeSync(recipeId);

        int randomItem = pickRandomItem(0,  ingredientList.size());

        onView(ViewMatchers.withId(R.id.rv_ingredients))
                .perform(RecyclerViewActions.actionOnItemAtPosition(randomItem, click()));

    }

}
