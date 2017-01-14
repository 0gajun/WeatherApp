/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.presentation.di.component;

import android.app.Activity;

import dagger.Component;
import io.github.a0gajun.weather.presentation.di.PerActivity;
import io.github.a0gajun.weather.presentation.di.module.ActivityModule;

/**
 * Created by Junya Ogasawara on 1/14/17.
 */

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {
    Activity activity();
}
