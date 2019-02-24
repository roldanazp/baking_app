package com.roldannanodegre.bakingapp;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

public class IngredientListActivity extends AbstractActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.activity_ingredient_list);

    }

}
