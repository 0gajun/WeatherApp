/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.presentation.view;

import java.util.Collection;

import io.github.a0gajun.weather.domain.model.CurrentWeatherAndForecast;

/**
 * Created by Junya Ogasawara on 1/14/17.
 */

public interface HomeView {
    void renderWeathers(Collection<CurrentWeatherAndForecast> currentWeatherAndForecast);
}
