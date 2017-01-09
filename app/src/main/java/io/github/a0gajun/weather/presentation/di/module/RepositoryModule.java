/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.presentation.di.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.github.a0gajun.weather.data.repository.WeatherRepositoryImpl;
import io.github.a0gajun.weather.domain.repository.WeatherRepository;

/**
 * Created by Junya Ogasawara on 1/9/17.
 */

// TODO: Put these provider into appropriate module
@Module
public class RepositoryModule {
    @Provides
    @Singleton
    WeatherRepository provideWeatherRepository(WeatherRepositoryImpl weatherRepositoryImpl) {
        return weatherRepositoryImpl;
    }
}
