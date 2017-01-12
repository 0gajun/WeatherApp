/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.data.repository.datasource;

import io.github.a0gajun.weather.data.entity.CurrentWeatherDataEntity;
import io.github.a0gajun.weather.data.entity.FiveDayWeatherForecastDataEntity;
import rx.Observable;

/**
 * Created by Junya Ogasawara on 1/9/17.
 */

public interface WeatherDataStore {
    Observable<CurrentWeatherDataEntity> currentWeatherEntityWithCityName(final String cityName, final String countryCode);

    Observable<CurrentWeatherDataEntity> currentWeatherEntityWithZipCode(final String zipCode, final String countryCode);

    Observable<FiveDayWeatherForecastDataEntity> fiveDayWeatherForecastEntityWithCityName(final String cityName, final String countryCode);

    Observable<FiveDayWeatherForecastDataEntity> fiveDayWeatherForecastEntityWithZipCode(final String zipCode, final String countryCode);
}
