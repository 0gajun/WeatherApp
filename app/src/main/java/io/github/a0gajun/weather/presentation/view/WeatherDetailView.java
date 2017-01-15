/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.presentation.view;

import io.github.a0gajun.weather.domain.model.CurrentWeatherAndForecast;
import io.github.a0gajun.weather.domain.model.GeocodingResult;

/**
 * Created by Junya Ogasawara on 1/15/17.
 */

public interface WeatherDetailView {
    void renderLocationInfo(GeocodingResult geocodingResult);

    void renderWeathers(CurrentWeatherAndForecast currentWeatherAndForecast);
}
