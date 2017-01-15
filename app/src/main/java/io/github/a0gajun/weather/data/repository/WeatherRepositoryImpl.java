/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.data.repository;

import java.util.regex.Pattern;

import javax.inject.Inject;

import io.github.a0gajun.weather.data.entity.mapper.CurrentWeatherMapper;
import io.github.a0gajun.weather.data.entity.mapper.FiveDayForecastMapper;
import io.github.a0gajun.weather.data.repository.datasource.WeatherDataStoreFactory;
import io.github.a0gajun.weather.domain.model.CurrentWeather;
import io.github.a0gajun.weather.domain.model.FiveDayForecast;
import io.github.a0gajun.weather.domain.repository.WeatherRepository;
import io.github.a0gajun.weather.domain.util.ZipCodeUtil;
import rx.Observable;

/**
 * Created by Junya Ogasawara on 1/9/17.
 */

public class WeatherRepositoryImpl implements WeatherRepository {

    private final static Pattern ZIP_CODE_PATTERN = Pattern.compile("\\d{3}-?\\d{4}");

    private final WeatherDataStoreFactory weatherDataStoreFactory;
    private final CurrentWeatherMapper currentWeatherMapper;
    private final FiveDayForecastMapper fiveDayForecastMapper;

    @Inject
    WeatherRepositoryImpl(WeatherDataStoreFactory weatherDataStoreFactory,
                          CurrentWeatherMapper currentWeatherMapper,
                          FiveDayForecastMapper fiveDayForecastMapper) {
        this.weatherDataStoreFactory = weatherDataStoreFactory;
        this.currentWeatherMapper = currentWeatherMapper;
        this.fiveDayForecastMapper = fiveDayForecastMapper;
    }

    @Override
    public Observable<CurrentWeather> currentWeather(String zipCode) {
        String formattedZipCode;
        try {
            formattedZipCode = ZipCodeUtil.formatZipCode(zipCode);
        } catch (ZipCodeUtil.InvalidZipCodeFormatException e) {
            return Observable.error(e);
        }

        return this.weatherDataStoreFactory.create()
                .currentWeatherEntityWithZipCode(formattedZipCode, "JP") // TODO: Remove hardcoded country code
                .map(this.currentWeatherMapper::transform);
    }

    @Override
    public Observable<FiveDayForecast> fiveDayForecast(String zipCode) {
        String formattedZipCode;
        try {
            formattedZipCode = ZipCodeUtil.formatZipCode(zipCode);
        } catch (ZipCodeUtil.InvalidZipCodeFormatException e) {
            return Observable.error(e);
        }

        return this.weatherDataStoreFactory.create()
                .fiveDayWeatherForecastEntityWithZipCode(formattedZipCode, "JP") // TODO: Remove hardcoded country code
                .map(this.fiveDayForecastMapper::transform);
    }
}
