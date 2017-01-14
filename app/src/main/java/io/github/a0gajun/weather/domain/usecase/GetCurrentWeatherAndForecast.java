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
 * Created by Junya Ogasawara on 1/14/17.
 */

public class GetCurrentWeatherAndForecast extends UseCase {

    private final WeatherRepository weatherRepository;

    private String zipCode;

    @Inject
    public GetCurrentWeatherAndForecast(ThreadExecutor threadExecutor,
                                        PostExecutionThread postExecutionThread,
                                        WeatherRepository weatherRepository) {
        super(threadExecutor, postExecutionThread);
        this.weatherRepository = weatherRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        if (this.zipCode == null) {
            return Observable.error(new IllegalArgumentException("You should set ZipCode before using this UseCase."));
        }

        Observable<CurrentWeather> currentWeatherObservable = this.weatherRepository.currentWeather(zipCode);
        Observable<FiveDayForecast> fiveDayForecastObservable = this.weatherRepository.fiveDayForecast(zipCode);

        return Observable.zip(currentWeatherObservable, fiveDayForecastObservable, CurrentWeatherAndForecast::new);
    }

    public void setZipCode(final String zipCode) {
        this.zipCode = zipCode;
    }
}
