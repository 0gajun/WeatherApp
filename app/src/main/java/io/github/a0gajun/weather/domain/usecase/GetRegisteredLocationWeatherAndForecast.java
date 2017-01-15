/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.domain.usecase;

import android.content.Context;

import io.github.a0gajun.weather.domain.executor.PostExecutionThread;
import io.github.a0gajun.weather.domain.executor.ThreadExecutor;
import io.github.a0gajun.weather.domain.model.CurrentWeather;
import io.github.a0gajun.weather.domain.model.CurrentWeatherAndForecast;
import io.github.a0gajun.weather.domain.model.FiveDayForecast;
import io.github.a0gajun.weather.domain.model.WatchingLocation;
import io.github.a0gajun.weather.domain.repository.WatchingLocationRepository;
import io.github.a0gajun.weather.domain.repository.WeatherRepository;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by Junya Ogasawara on 1/14/17.
 */

public class GetRegisteredLocationWeatherAndForecast extends UseCase {

    private final Context context;
    private final WeatherRepository weatherRepository;
    private final WatchingLocationRepository watchingLocationRepository;

    public GetRegisteredLocationWeatherAndForecast(Context context,
                                                   WeatherRepository weatherRepository,
                                                   WatchingLocationRepository watchingLocationRepository,
                                                   ThreadExecutor threadExecutor,
                                                   PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.context = context;
        this.weatherRepository = weatherRepository;
        this.watchingLocationRepository = watchingLocationRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return this.getRegisteredZipCodeObservable()
                .observeOn(Schedulers.from(this.threadExecutor))
                .flatMap(zipCode -> {
                    Observable<CurrentWeather> currentWeatherObservable = this.weatherRepository.currentWeather(zipCode);
                    Observable<FiveDayForecast> fiveDayForecastObservable = this.weatherRepository.fiveDayForecast(zipCode);

                    return Observable.zip(currentWeatherObservable,
                            fiveDayForecastObservable,
                            ((currentWeather, forecast) -> new CurrentWeatherAndForecast(currentWeather, forecast, zipCode)));
                })
                .toList();
    }

    private Observable<String> getRegisteredZipCodeObservable() {
        return this.watchingLocationRepository.getAllWatchingLocations()
                .sorted((e1, e2) -> e1.getPriority() > e2.getPriority() ? 1 : -1)
                .map(WatchingLocation::getZipCode);
    }
}