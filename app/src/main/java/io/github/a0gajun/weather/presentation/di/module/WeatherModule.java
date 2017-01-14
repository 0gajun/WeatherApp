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
import io.github.a0gajun.weather.domain.repository.WatchingLocationRepository;
import io.github.a0gajun.weather.domain.repository.WeatherRepository;
import io.github.a0gajun.weather.domain.usecase.GetCurrentLocationWeatherAndForecast;
import io.github.a0gajun.weather.domain.usecase.GetCurrentWeather;
import io.github.a0gajun.weather.domain.usecase.GetFiveDayForecast;
import io.github.a0gajun.weather.domain.usecase.GetRegisteredLocationWeatherAndForecast;
import io.github.a0gajun.weather.domain.usecase.RegisterWatchingLocation;
import io.github.a0gajun.weather.domain.usecase.UseCase;
import io.github.a0gajun.weather.presentation.di.PerActivity;

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
    @Named(Qualifiers.CURRENT_LOCATION_WEATHER_FORECAST)
    UseCase provideGetCurrentLocationWeatherAndForecast(Context context,
                                                        WeatherRepository weatherRepository,
                                                        LocationRepository locationRepository,
                                                        ThreadExecutor threadExecutor,
                                                        PostExecutionThread postExecutionThread) {
        return new GetCurrentLocationWeatherAndForecast(context,
                threadExecutor, postExecutionThread,
                locationRepository, weatherRepository);
    }

    @Provides
    @PerActivity
    @Named(Qualifiers.REGISTERED_LOCATION_WEATHER_FORECAST)
    UseCase provideGetRegisteredLocationWeatherAndForecast(Context context,
                                                           WeatherRepository weatherRepository,
                                                           ThreadExecutor threadExecutor,
                                                           PostExecutionThread postExecutionThread) {
        return new GetRegisteredLocationWeatherAndForecast(context, weatherRepository, threadExecutor, postExecutionThread);
    }

    @Provides
    @PerActivity
    @Named(Qualifiers.REGISTER_WATCHING_LOCATION)
    RegisterWatchingLocation provideRegisterWatchingLocation(WatchingLocationRepository watchingLocationRepository,
                                                             ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        return new RegisterWatchingLocation(watchingLocationRepository, threadExecutor, postExecutionThread);
    }
}
