/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.data.repository.datasource;

import io.github.a0gajun.weather.data.entity.CurrentWeatherDataEntity;
import io.github.a0gajun.weather.data.entity.FiveDayWeatherForecastDataEntity;
import io.github.a0gajun.weather.data.net.OpenWeatherMapApi;
import rx.Observable;

/**
 * Created by Junya Ogasawara on 1/9/17.
 */

public class CloudWeatherDataStore implements WeatherDataStore {

    private final OpenWeatherMapApi openWeatherMapApi;

    public CloudWeatherDataStore(OpenWeatherMapApi openWeatherMapApi) {
        this.openWeatherMapApi = openWeatherMapApi;
    }

    @Override
    public Observable<CurrentWeatherDataEntity> currentWeatherEntity(String cityName, String countryCode) {
        return this.openWeatherMapApi.getCurrentWeather(constructQueryParameter(cityName, countryCode));
    }

    @Override
    public Observable<FiveDayWeatherForecastDataEntity> fiveDayWeatherForecastEntity(String cityName, String countryCode) {
        return this.openWeatherMapApi.getFiveDayWeatherForecast(constructQueryParameter(cityName, countryCode));
    }

    private String constructQueryParameter(final String cityName, final String countryCode) {
        return cityName + "," + countryCode;
    }
}
