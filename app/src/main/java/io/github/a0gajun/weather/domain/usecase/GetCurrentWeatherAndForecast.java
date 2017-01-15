/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.domain.usecase;

import javax.inject.Inject;

import io.github.a0gajun.weather.domain.executor.PostExecutionThread;
import io.github.a0gajun.weather.domain.executor.ThreadExecutor;
import io.github.a0gajun.weather.domain.model.CurrentWeather;
import io.github.a0gajun.weather.domain.model.CurrentWeatherAndForecast;
import io.github.a0gajun.weather.domain.model.FiveDayForecast;
import io.github.a0gajun.weather.domain.repository.GeocodingRepository;
import io.github.a0gajun.weather.domain.repository.WeatherRepository;
import rx.Observable;

/**
 * Created by Junya Ogasawara on 1/15/17.
 */

public class GetCurrentWeatherAndForecast extends UseCase {

    private final WeatherRepository weatherRepository;
    private final GeocodingRepository geocodingRepository;

    private String zipCode;

    @Inject
    public GetCurrentWeatherAndForecast(WeatherRepository weatherRepository,
                                        GeocodingRepository geocodingRepository,
                                        ThreadExecutor threadExecutor,
                                        PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.weatherRepository = weatherRepository;
        this.geocodingRepository = geocodingRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return this.geocodingRepository.geocodingWithZipCode(this.zipCode)
                .flatMap(geocodingResult -> {
                    Observable<CurrentWeather> currentWeatherObservable = this.weatherRepository.currentWeather(zipCode);
                    Observable<FiveDayForecast> fiveDayForecastObservable = this.weatherRepository.fiveDayForecast(zipCode);

                    return Observable.zip(currentWeatherObservable, fiveDayForecastObservable,
                            ((currentWeather, forecast)
                                    -> new CurrentWeatherAndForecast(currentWeather, forecast, zipCode,
                                    geocodingResult.getCountryName(), geocodingResult.getCityName())));
                });
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
}
