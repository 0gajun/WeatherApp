/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.presentation.di.module;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.github.a0gajun.weather.data.net.OpenWeatherMapApi;
import io.github.a0gajun.weather.data.repository.WeatherRepositoryImpl;
import io.github.a0gajun.weather.domain.executor.PostExecutionThread;
import io.github.a0gajun.weather.domain.executor.ThreadExecutor;
import io.github.a0gajun.weather.domain.repository.WeatherRepository;
import io.github.a0gajun.weather.domain.usecase.GetCurrentWeather;
import io.github.a0gajun.weather.domain.usecase.GetFiveDayForecast;
import io.github.a0gajun.weather.domain.usecase.UseCase;
import io.github.a0gajun.weather.presentation.di.PerActivity;
import retrofit2.Retrofit;

/**
 * Dagger module that provides weather related collaborators.
 * <p>
 * Created by Junya Ogasawara on 1/9/17.
 */

@Module
public class WeatherModule extends NetModule {

    private final String zipCode;

    public WeatherModule(String baseUrl, String zipCode) {
        super(baseUrl);
        this.zipCode = zipCode;
    }

    @Provides
    @PerActivity
    OpenWeatherMapApi provideOpenWeatherApi(Retrofit retrofit) {
        return retrofit.create(OpenWeatherMapApi.class);
    }

    @Provides
    @Singleton
    WeatherRepository provideWeatherRepository(WeatherRepositoryImpl weatherRepositoryImpl) {
        return weatherRepositoryImpl;
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
}
