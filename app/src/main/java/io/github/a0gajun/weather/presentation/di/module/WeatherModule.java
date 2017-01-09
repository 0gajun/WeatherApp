/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.presentation.di.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.github.a0gajun.weather.data.net.OpenWeatherMapApi;
import io.github.a0gajun.weather.data.repository.WeatherRepositoryImpl;
import io.github.a0gajun.weather.domain.repository.WeatherRepository;
import io.github.a0gajun.weather.presentation.di.PerActivity;
import retrofit2.Retrofit;

/**
 * Dagger module that provides weather related collaborators.
 * <p>
 * Created by Junya Ogasawara on 1/9/17.
 */

@Module
public class WeatherModule extends NetModule {
    public WeatherModule(String baseUrl) {
        super(baseUrl);
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

}
