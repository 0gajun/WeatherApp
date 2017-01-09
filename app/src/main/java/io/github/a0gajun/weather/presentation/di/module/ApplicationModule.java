/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.presentation.di.module;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.github.a0gajun.weather.AndroidApplication;

/**
 * Created by Junya Ogasawara on 1/9/17.
 */

@Module
public class ApplicationModule {
    private final AndroidApplication androidApplication;

    public ApplicationModule(AndroidApplication androidApplication) {
        this.androidApplication = androidApplication;
    }

    @Provides
    @Singleton
    Application provideApplication() {
        return this.androidApplication;
    }

    @Provides
    @Singleton
    Context provideApplicationContext() {
        return this.androidApplication;
    }
}
