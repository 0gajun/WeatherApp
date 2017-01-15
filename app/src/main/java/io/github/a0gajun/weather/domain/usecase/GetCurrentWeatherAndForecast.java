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
import io.github.a0gajun.weather.domain.repository.WeatherRepository;
import rx.Observable;

/**
 * Created by Junya Ogasawara on 1/15/17.
 */

public class GetCurrentWeatherAndForecast extends UseCase {

    private final WeatherRepository weatherRepository;

    private String zipCode;

    @Inject
    public GetCurrentWeatherAndForecast(WeatherRepository weatherRepository, ThreadExecutor threadExecutor,
                                        PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.weatherRepository = weatherRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        Observable<CurrentWeather> currentWeatherObservable = this.weatherRepository.currentWeather(zipCode);
        Observable<FiveDayForecast> fiveDayForecastObservable = this.weatherRepository.fiveDayForecast(zipCode);

        return Observable.zip(currentWeatherObservable, fiveDayForecastObservable,
                ((currentWeather, forecast) -> new CurrentWeatherAndForecast(currentWeather, forecast, zipCode)));
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
}
