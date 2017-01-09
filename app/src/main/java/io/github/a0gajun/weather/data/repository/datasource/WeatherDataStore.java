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
    Observable<CurrentWeatherDataEntity> currentWeatherEntity(final String cityName, final String countryCode);

    Observable<FiveDayWeatherForecastDataEntity> fiveDayWeatherForecastEntity(final String cityName, final String countryCode);
}
