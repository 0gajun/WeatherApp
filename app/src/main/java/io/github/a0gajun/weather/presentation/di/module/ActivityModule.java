/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.presentation.di.module;

import android.app.Activity;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import io.github.a0gajun.weather.domain.executor.PostExecutionThread;
import io.github.a0gajun.weather.domain.executor.ThreadExecutor;
import io.github.a0gajun.weather.domain.repository.GeocodingRepository;
import io.github.a0gajun.weather.domain.repository.WatchingLocationRepository;
import io.github.a0gajun.weather.domain.usecase.GeocodingUsingZipCode;
import io.github.a0gajun.weather.domain.usecase.RegisterWatchingLocation;
import io.github.a0gajun.weather.presentation.di.PerActivity;

/**
 * Created by Junya Ogasawara on 1/14/17.
 */

@Module
public class ActivityModule {

    private final Activity activity;

    public ActivityModule(Activity activity) {
        this.activity = activity;
    }

    @Provides
    @PerActivity
    Activity provideActivity() {
        return this.activity;
    }

    @Provides
    @PerActivity
    GeocodingUsingZipCode provideGeocodingUsingZipCode(GeocodingRepository geocodingRepository,
                                                       ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        return new GeocodingUsingZipCode(geocodingRepository, threadExecutor, postExecutionThread);
    }

    @Provides
    @PerActivity
    @Named(Qualifiers.REGISTER_WATCHING_LOCATION)
    RegisterWatchingLocation provideRegisterWatchingLocation(WatchingLocationRepository watchingLocationRepository,
                                                             ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        return new RegisterWatchingLocation(watchingLocationRepository, threadExecutor, postExecutionThread);
    }
}
