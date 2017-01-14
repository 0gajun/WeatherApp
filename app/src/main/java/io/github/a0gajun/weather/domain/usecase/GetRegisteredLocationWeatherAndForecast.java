/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.domain.usecase;

import android.content.Context;

import java.util.Arrays;
import java.util.List;

import io.github.a0gajun.weather.domain.executor.PostExecutionThread;
import io.github.a0gajun.weather.domain.executor.ThreadExecutor;
import io.github.a0gajun.weather.domain.model.CurrentWeather;
import io.github.a0gajun.weather.domain.model.CurrentWeatherAndForecast;
import io.github.a0gajun.weather.domain.model.FiveDayForecast;
import io.github.a0gajun.weather.domain.repository.WeatherRepository;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by Junya Ogasawara on 1/14/17.
 */

public class GetRegisteredLocationWeatherAndForecast extends UseCase {

    private final Context context;
    private final WeatherRepository weatherRepository;

    public GetRegisteredLocationWeatherAndForecast(Context context,
                                                   WeatherRepository weatherRepository,
                                                   ThreadExecutor threadExecutor,
                                                   PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.context = context;
        this.weatherRepository = weatherRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return Observable.from(getRegisteredZipCodeList())
                .observeOn(Schedulers.from(this.threadExecutor))
                .flatMap(zipCode -> {
                    Observable<CurrentWeather> currentWeatherObservable = this.weatherRepository.currentWeather(zipCode);
                    Observable<FiveDayForecast> fiveDayForecastObservable = this.weatherRepository.fiveDayForecast(zipCode);

                    return Observable.zip(currentWeatherObservable, fiveDayForecastObservable, CurrentWeatherAndForecast::new);
                })
                .toList();
    }

    private List<String> getRegisteredZipCodeList() {
        return Arrays.asList("223-0061", "150-0013", "192-0353", "206-0012");

    }
}