/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.presentation.di.component;

import dagger.Component;
import io.github.a0gajun.weather.presentation.di.PerActivity;
import io.github.a0gajun.weather.presentation.di.module.WeatherModule;

/**
 * Created by Junya Ogasawara on 1/9/17.
 */

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {WeatherModule.class})
public interface WeatherComponent {

}
