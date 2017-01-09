/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.domain.usecase;

import javax.inject.Inject;

import io.github.a0gajun.weather.domain.executor.PostExecutionThread;
import io.github.a0gajun.weather.domain.executor.ThreadExecutor;
import io.github.a0gajun.weather.domain.repository.WeatherRepository;
import rx.Observable;

/**
 * Created by Junya Ogasawara on 1/9/17.
 */

public class GetFiveDayForecast extends UseCase {

    private final String zipCode;
    private final WeatherRepository weatherRepository;

    @Inject
    public GetFiveDayForecast(final String zipCode,
                             final WeatherRepository weatherRepository,
                             final ThreadExecutor threadExecutor,
                             final PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.zipCode = zipCode;
        this.weatherRepository = weatherRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return this.weatherRepository.fiveDayForecast(this.zipCode);
    }
}
