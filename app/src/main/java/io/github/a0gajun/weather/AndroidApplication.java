/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.jakewharton.threetenabp.AndroidThreeTen;
import com.squareup.leakcanary.LeakCanary;

import io.github.a0gajun.weather.data.entity.OrmaDatabase;
import io.github.a0gajun.weather.presentation.di.component.ApplicationComponent;
import io.github.a0gajun.weather.presentation.di.component.DaggerApplicationComponent;
import io.github.a0gajun.weather.presentation.di.module.ApplicationModule;
import io.github.a0gajun.weather.presentation.di.module.NetModule;
import io.github.a0gajun.weather.presentation.di.module.RepositoryModule;
import timber.log.Timber;

/**
 * Created by Junya Ogasawara on 1/9/17.
 */

public class AndroidApplication extends Application {

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        this.initializeTimber();
        initializeInjector();
        this.initializeLeakDetection();
        Stetho.initializeWithDefaults(this);
        AndroidThreeTen.init(this);
    }

    private void initializeInjector() {
        this.applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .repositoryModule(new RepositoryModule())
                .netModule(new NetModule())
                .build();
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
            Timber.d("Leak canary is enabled!!!");
            LeakCanary.install(this);
        }
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}
