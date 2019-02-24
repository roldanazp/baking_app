package com.roldannanodegre.bakingapp.suites;

import com.roldannanodegre.bakingapp.test.IngredientListActivityInstrumentedTest;
import com.roldannanodegre.bakingapp.test.RecipeListActivityInstrumentedTest;
import com.roldannanodegre.bakingapp.test.StepListActivityTabletInstrumentedTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        RecipeListActivityInstrumentedTest.class,
        StepListActivityTabletInstrumentedTest.class,
        IngredientListActivityInstrumentedTest.class
})

public class TabletTestSuite {
}
