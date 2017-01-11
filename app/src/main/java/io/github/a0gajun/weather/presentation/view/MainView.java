/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.presentation.view;

import java.util.Collection;

import io.github.a0gajun.weather.domain.model.CurrentWeather;
import io.github.a0gajun.weather.domain.model.FiveDayForecast;

/**
 * Created by Junya Ogasawara on 1/9/17.
 */

//TODO: Consider granularity of view
public interface MainView {
    void renderCurrentWeather(CurrentWeather currentWeather);

    void renderEveryThreeHoursForecast(Collection<FiveDayForecast.EveryThreeHoursForecastData> forecastData);
}
