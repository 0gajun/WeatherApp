/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.presentation.di.component;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import io.github.a0gajun.weather.presentation.di.module.ApplicationModule;
import io.github.a0gajun.weather.presentation.view.activity.BaseActivity;

/**
 * Created by Junya Ogasawara on 1/9/17.
 */

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    void inject(BaseActivity baseActivity);

    // --- Expose dependee component --- //

    // ApplicationModule
    Application application();

    Context context();
}
