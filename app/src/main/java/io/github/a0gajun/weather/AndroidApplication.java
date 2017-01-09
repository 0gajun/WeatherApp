/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather;

import android.app.Application;

import com.jakewharton.threetenabp.AndroidThreeTen;
import com.squareup.leakcanary.LeakCanary;

import timber.log.Timber;

/**
 * Created by Junya Ogasawara on 1/9/17.
 */

public class AndroidApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        this.initializeLeakDetection();
        this.initializeTimber();
        AndroidThreeTen.init(this);
    }

    private void initializeTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    private void initializeLeakDetection() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        if (BuildConfig.DEBUG) {
            LeakCanary.install(this);
        }
    }
}
