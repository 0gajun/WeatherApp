/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.data.repository;

import android.support.v4.util.Pair;

import io.github.a0gajun.weather.data.entity.mapper.CurrentWeatherMapper;
import io.github.a0gajun.weather.data.entity.mapper.FiveDayForecastMapper;
import io.github.a0gajun.weather.data.repository.datasource.WeatherDataStoreFactory;
import io.github.a0gajun.weather.domain.model.CurrentWeather;
import io.github.a0gajun.weather.domain.model.FiveDayForecast;
import io.github.a0gajun.weather.domain.repository.WeatherRepository;
import rx.Observable;

/**
 * Created by Junya Ogasawara on 1/9/17.
 */

public class WeatherRepositoryImpl implements WeatherRepository {

    private final WeatherDataStoreFactory weatherDataStoreFactory;
    private final CurrentWeatherMapper currentWeatherMapper;
    private final FiveDayForecastMapper fiveDayForecastMapper;

    public WeatherRepositoryImpl(WeatherDataStoreFactory weatherDataStoreFactory,
                                 CurrentWeatherMapper currentWeatherMapper,
                                 FiveDayForecastMapper fiveDayForecastMapper) {
        this.weatherDataStoreFactory = weatherDataStoreFactory;
        this.currentWeatherMapper = currentWeatherMapper;
        this.fiveDayForecastMapper = fiveDayForecastMapper;
    }

    @Override
    public Observable<CurrentWeather> currentWeather(String zipCode) {
        final Pair<String, String> cityNameCountryCodePair = converZipCodeToCityNameAndCountryCode(zipCode);

        return this.weatherDataStoreFactory.create()
                .currentWeatherEntity(cityNameCountryCodePair.first, cityNameCountryCodePair.second)
                .map(this.currentWeatherMapper::transform);
    }

    @Override
    public Observable<FiveDayForecast> fiveDayForecast(String zipCode) {
        final Pair<String, String> cityNameCountryCodePair = converZipCodeToCityNameAndCountryCode(zipCode);

        return this.weatherDataStoreFactory.create()
                .fiveDayWeatherForecastEntity(cityNameCountryCodePair.first, cityNameCountryCodePair.second)
                .map(this.fiveDayForecastMapper::transform);
    }


    private Pair<String, String> converZipCodeToCityNameAndCountryCode(final String zipCode) {
        // TODO: implement
        return new Pair<>("Kawasaki", "JP");
    }
}
