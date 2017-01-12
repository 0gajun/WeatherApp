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

class CloudWeatherDataStore implements WeatherDataStore {

    private final OpenWeatherMapApi openWeatherMapApi;

    CloudWeatherDataStore(OpenWeatherMapApi openWeatherMapApi) {
        this.openWeatherMapApi = openWeatherMapApi;
    }

    @Override
    public Observable<CurrentWeatherDataEntity> currentWeatherEntityWithCityName(String cityName, String countryCode) {
        return this.openWeatherMapApi.getCurrentWeather(constructQueryParameter(cityName, countryCode));
    }

    @Override
    public Observable<FiveDayWeatherForecastDataEntity> fiveDayWeatherForecastEntityWithCityName(String cityName, String countryCode) {
        return this.openWeatherMapApi.getFiveDayWeatherForecast(constructQueryParameter(cityName, countryCode));
    }

    @Override
    public Observable<CurrentWeatherDataEntity> currentWeatherEntityWithZipCode(String zipCode, String countryCode) {
        return this.openWeatherMapApi.getCurrentWeatherByZipCode(constructQueryParameter(zipCode, countryCode));
    }

    @Override
    public Observable<FiveDayWeatherForecastDataEntity> fiveDayWeatherForecastEntityWithZipCode(String zipCode, String countryCode) {
        return this.openWeatherMapApi.getFiveDayWeatherForecastByZipCode(constructQueryParameter(zipCode, countryCode));
    }

    private String constructQueryParameter(final String query, final String countryCode) {
        return query + "," + countryCode;
    }
}
