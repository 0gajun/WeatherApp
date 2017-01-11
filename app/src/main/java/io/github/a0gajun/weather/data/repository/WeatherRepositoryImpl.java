/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.data.repository;

import android.support.v4.util.Pair;

import java.util.List;

import javax.inject.Inject;

import io.github.a0gajun.weather.data.entity.GoogleMapsGeocodingEntity;
import io.github.a0gajun.weather.data.entity.mapper.CurrentWeatherMapper;
import io.github.a0gajun.weather.data.entity.mapper.FiveDayForecastMapper;
import io.github.a0gajun.weather.data.exception.ZipCodeNotResolvedException;
import io.github.a0gajun.weather.data.repository.datasource.GeocodingDataStoreFactory;
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
    private final GeocodingDataStoreFactory geocodingDataStoreFactory;
    private final CurrentWeatherMapper currentWeatherMapper;
    private final FiveDayForecastMapper fiveDayForecastMapper;

    @Inject
    WeatherRepositoryImpl(WeatherDataStoreFactory weatherDataStoreFactory,
                          GeocodingDataStoreFactory geocodingDataStoreFactory,
                          CurrentWeatherMapper currentWeatherMapper,
                          FiveDayForecastMapper fiveDayForecastMapper) {
        this.weatherDataStoreFactory = weatherDataStoreFactory;
        this.geocodingDataStoreFactory = geocodingDataStoreFactory;
        this.currentWeatherMapper = currentWeatherMapper;
        this.fiveDayForecastMapper = fiveDayForecastMapper;
    }

    @Override
    public Observable<CurrentWeather> currentWeather(String zipCode) {
        return convertZipCodeToCityNameAndCountryCodeObservable(zipCode)
                .flatMap(e ->
                        this.weatherDataStoreFactory.create()
                                .currentWeatherEntity(e.first, e.second)
                                .map(this.currentWeatherMapper::transform)
                );
    }

    @Override
    public Observable<FiveDayForecast> fiveDayForecast(String zipCode) {
        return convertZipCodeToCityNameAndCountryCodeObservable(zipCode)
                .flatMap(e ->
                        this.weatherDataStoreFactory.create()
                                .fiveDayWeatherForecastEntity(e.first, e.second)
                                .map(this.fiveDayForecastMapper::transform)
                );
    }

    /**
     * Convert zip-code into City name and Country code using Google Maps geocoding api.
     *
     * @param zipCode target location's zipcode
     * @return a pair of city name and country code.
     */
    private Observable<Pair<String, String>> convertZipCodeToCityNameAndCountryCodeObservable(final String zipCode) {
        return this.geocodingDataStoreFactory.create()
                .geocodingEntityOfZipCode(zipCode)
                .map(this::extractCityNameCountryCode);
    }

    /**
     * Extract city name and country code from {@link GoogleMapsGeocodingEntity}.
     *
     * @param entity {@link GoogleMapsGeocodingEntity}
     * @return p pair of city name and country code
     * @throws ZipCodeNotResolvedException this exception will be thrown if zip code isn't found.
     */
    private Pair<String, String> extractCityNameCountryCode(final GoogleMapsGeocodingEntity entity) throws ZipCodeNotResolvedException {
        if (entity.getResults().size() != 1) {
            throw new ZipCodeNotResolvedException();
        }
        final GoogleMapsGeocodingEntity.Result result = entity.getResults().get(0);

        String countryCode = "";
        String cityName = "";
        for (GoogleMapsGeocodingEntity.AddressComponent ac : result.getAddressComponents()) {
            final List<String> types = ac.getTypes();
            if (types.contains("country")) { // Country
                countryCode = ac.getShortName();
            }
            if (types.contains("locality")) { // City
                cityName = ac.getShortName();
            }
        }

        if (countryCode.isEmpty() || cityName.isEmpty()) {
            throw new ZipCodeNotResolvedException();
        }

        return new Pair<>(cityName, countryCode);
    }
}
