/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.domain.model;

import lombok.Getter;

/**
 * Created by Junya Ogasawara on 1/14/17.
 */

public class CurrentWeatherAndForecast {
    @Getter private final CurrentWeather currentWeather;
    @Getter private final FiveDayForecast fiveDayForecast;

    public CurrentWeatherAndForecast(CurrentWeather currentWeather, FiveDayForecast fiveDayForecast) {
        this.currentWeather = currentWeather;
        this.fiveDayForecast = fiveDayForecast;
    }
}
