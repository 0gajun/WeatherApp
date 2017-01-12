/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.domain.usecase;

import android.location.Location;

import javax.inject.Inject;

import io.github.a0gajun.weather.domain.executor.PostExecutionThread;
import io.github.a0gajun.weather.domain.executor.ThreadExecutor;
import io.github.a0gajun.weather.domain.repository.LocationRepository;
import rx.Observable;

/**
 * Created by Junya Ogasawara on 1/12/17.
 */

public class GetCurrentLocation extends UseCase {

    private final LocationRepository locationRepository;

    @Inject
    public GetCurrentLocation(ThreadExecutor threadExecutor,
                              PostExecutionThread postExecutionThread,
                              LocationRepository locationRepository) {
        super(threadExecutor, postExecutionThread);
        this.locationRepository = locationRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return this.locationRepository.startLocationUpdates();
    }

    @Override
    public void unsubscribe() {
        super.unsubscribe();
        this.locationRepository.stopLocationUpdates();
    }

    public Location getCurrentLocation() {
        return this.locationRepository.getCurrentLocation();
    }
}
