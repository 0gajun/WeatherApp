/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.domain.usecase;

import javax.inject.Inject;

import io.github.a0gajun.weather.domain.executor.PostExecutionThread;
import io.github.a0gajun.weather.domain.executor.ThreadExecutor;
import io.github.a0gajun.weather.domain.model.WatchingLocation;
import io.github.a0gajun.weather.domain.repository.WatchingLocationRepository;
import rx.Observable;

/**
 * Created by Junya Ogasawara on 1/14/17.
 */

public class RegisterWatchingLocation extends UseCase {

    private final WatchingLocationRepository watchingLocationRepository;

    private WatchingLocation watchingLocation;

    @Inject
    public RegisterWatchingLocation(WatchingLocationRepository watchingLocationRepository,
                                    ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.watchingLocationRepository = watchingLocationRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return this.watchingLocationRepository.registerOrUpdateWatchingLocation(this.watchingLocation);
    }

    public void setWatchingLocation(WatchingLocation watchingLocation) {
        this.watchingLocation = watchingLocation;
    }
}
