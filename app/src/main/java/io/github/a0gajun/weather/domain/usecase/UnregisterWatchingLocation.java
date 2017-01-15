/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.domain.usecase;

import io.github.a0gajun.weather.domain.executor.PostExecutionThread;
import io.github.a0gajun.weather.domain.executor.ThreadExecutor;
import io.github.a0gajun.weather.domain.repository.WatchingLocationRepository;
import rx.Observable;

/**
 * Created by Junya Ogasawara on 1/15/17.
 */

public class UnregisterWatchingLocation extends UseCase {
    private final WatchingLocationRepository watchingLocationRepository;

    private String zipCode;

    public UnregisterWatchingLocation(WatchingLocationRepository watchingLocationRepository,
                                      ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.watchingLocationRepository = watchingLocationRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return watchingLocationRepository.unregisterWatchingLocation(this.zipCode).toObservable();
    }

    public void setZipCode(final String zipCode) {
        this.zipCode = zipCode;
    }
}
