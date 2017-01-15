/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.domain.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Junya Ogasawara on 1/14/17.
 */

public class CurrentWeatherAndForecast {
    @Getter private final CurrentWeather currentWeather;
    @Getter private final FiveDayForecast fiveDayForecast;
    @Getter private final String zipCode;
    @Getter private final String countryName;
    @Getter private final String cityName;

    @Getter @Setter private boolean isCurrentLocation;

    public CurrentWeatherAndForecast(CurrentWeather currentWeather, FiveDayForecast fiveDayForecast,
                                     String zipCode, String countryName, String cityName) {
        this.currentWeather = currentWeather;
        this.fiveDayForecast = fiveDayForecast;
        this.zipCode = zipCode;
        this.countryName = countryName;
        this.cityName = cityName;
    }
}
