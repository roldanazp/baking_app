package com.roldannanodegre.bakingapp;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

public class StepListActivity extends AbstractActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.activity_step_list);
    }

}
