package com.roldannanodegre.bakingapp.fragments;

import android.support.v4.app.Fragment;

import com.roldannanodegre.bakingapp.AbstractActivity;

public abstract class AbstractFragment extends Fragment {

    void increment(String id){
        if (getActivity()!=null && getActivity() instanceof AbstractActivity) {
            ((AbstractActivity) getActivity()).increment(id);
        }
    }

    void decrement(String id){
        if (getActivity()!=null && getActivity() instanceof AbstractActivity) {
            ((AbstractActivity) getActivity()).decrement(id);
        }
    }


}
