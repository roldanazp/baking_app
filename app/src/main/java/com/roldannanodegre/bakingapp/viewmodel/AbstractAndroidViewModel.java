package com.roldannanodegre.bakingapp.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

public abstract class AbstractAndroidViewModel extends AndroidViewModel {

    public boolean isIdle;

    AbstractAndroidViewModel(@NonNull Application application) {
        super(application);
    }
}
