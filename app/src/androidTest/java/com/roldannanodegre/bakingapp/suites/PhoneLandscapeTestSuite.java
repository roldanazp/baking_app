package com.roldannanodegre.bakingapp.suites;

import com.roldannanodegre.bakingapp.test.IngredientListActivityInstrumentedTest;
import com.roldannanodegre.bakingapp.test.RecipeListActivityInstrumentedTest;
import com.roldannanodegre.bakingapp.test.StepDetailActivityLandInstrumentedTest;
import com.roldannanodegre.bakingapp.test.StepListActivityInstrumentedTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        RecipeListActivityInstrumentedTest.class,
        StepListActivityInstrumentedTest.class,
        IngredientListActivityInstrumentedTest.class,
        StepDetailActivityLandInstrumentedTest.class
})

public class PhoneLandscapeTestSuite {
}
