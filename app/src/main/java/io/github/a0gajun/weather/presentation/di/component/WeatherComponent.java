/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.presentation.di.component;

import dagger.Component;
import io.github.a0gajun.weather.presentation.di.PerActivity;
import io.github.a0gajun.weather.presentation.di.module.ActivityModule;
import io.github.a0gajun.weather.presentation.di.module.WeatherModule;
import io.github.a0gajun.weather.presentation.view.fragment.HomeFragment;
import io.github.a0gajun.weather.presentation.view.fragment.TodaysEventForecastFragment;
import io.github.a0gajun.weather.presentation.view.fragment.WeatherDetailFragment;

/**
 * Created by Junya Ogasawara on 1/9/17.
 */

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class, WeatherModule.class})
public interface WeatherComponent extends ActivityComponent {
    void inject(HomeFragment homeFragment);
    void inject(WeatherDetailFragment weatherDetailFragment);
    void inject(TodaysEventForecastFragment todaysEventForecastFragment);
}
