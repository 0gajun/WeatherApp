/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.domain.repository;

import io.github.a0gajun.weather.domain.model.CurrentWeather;
import io.github.a0gajun.weather.domain.model.FiveDayForecast;
import rx.Observable;

/**
 * Created by Junya Ogasawara on 1/9/17.
 */

public interface WeatherRepository {
    Observable<CurrentWeather> currentWeather(final String zipCode);

    Observable<FiveDayForecast> fiveDayForecast(final String zipCode);
}
