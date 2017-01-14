/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.presentation.di.module;

import android.content.Context;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import io.github.a0gajun.weather.domain.executor.PostExecutionThread;
import io.github.a0gajun.weather.domain.executor.ThreadExecutor;
import io.github.a0gajun.weather.domain.repository.LocationRepository;
import io.github.a0gajun.weather.domain.repository.WeatherRepository;
import io.github.a0gajun.weather.domain.usecase.GetCurrentLocationWeatherAndForecast;
import io.github.a0gajun.weather.domain.usecase.GetCurrentWeather;
import io.github.a0gajun.weather.domain.usecase.GetCurrentWeatherAndForecast;
import io.github.a0gajun.weather.domain.usecase.GetFiveDayForecast;
import io.github.a0gajun.weather.domain.usecase.UseCase;
import io.github.a0gajun.weather.presentation.di.PerActivity;

import static io.github.a0gajun.weather.presentation.di.module.Qualifiers.GET_CURRENT_WEATHER_AND_FORECAST;

/**
 * Dagger module that provides weather related collaborators.
 * <p>
 * Created by Junya Ogasawara on 1/9/17.
 */

@Module
public class WeatherModule {

    private final String zipCode;

    public WeatherModule(String zipCode) {
        this.zipCode = zipCode;
    }

    @Provides
    @PerActivity
    @Named("currentWeather")
    UseCase provideGetCurrentWeather(WeatherRepository weatherRepository, ThreadExecutor threadExecutor,
                                     PostExecutionThread postExecutionThread) {
        return new GetCurrentWeather(this.zipCode, weatherRepository, threadExecutor, postExecutionThread);
    }

    @Provides
    @PerActivity
    @Named("fiveDayForecast")
    UseCase provideGetFiveDayForecast(WeatherRepository weatherRepository, ThreadExecutor threadExecutor,
                                      PostExecutionThread postExecutionThread) {
        return new GetFiveDayForecast(this.zipCode, weatherRepository, threadExecutor, postExecutionThread);
    }

    @Provides
    @PerActivity
    @Named(GET_CURRENT_WEATHER_AND_FORECAST)
    GetCurrentWeatherAndForecast provideGetCurrentWeatherAndForecast(WeatherRepository weatherRepository, ThreadExecutor threadExecutor,
                                                PostExecutionThread postExecutionThread) {
        return new GetCurrentWeatherAndForecast(threadExecutor, postExecutionThread, weatherRepository);
    }

    @Provides
    @PerActivity
    GetCurrentLocationWeatherAndForecast provideGetCurrentLocationWeatherAndForecast(Context context,
                                                                                     WeatherRepository weatherRepository,
                                                                                     LocationRepository locationRepository,
                                                                                     ThreadExecutor threadExecutor,
                                                                                     PostExecutionThread postExecutionThread) {
        return new GetCurrentLocationWeatherAndForecast(context,
                threadExecutor, postExecutionThread,
                locationRepository, weatherRepository);
    }
}
