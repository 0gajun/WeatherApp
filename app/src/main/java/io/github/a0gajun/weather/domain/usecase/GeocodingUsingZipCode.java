/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.domain.usecase;

import javax.inject.Inject;

import io.github.a0gajun.weather.domain.executor.PostExecutionThread;
import io.github.a0gajun.weather.domain.executor.ThreadExecutor;
import io.github.a0gajun.weather.domain.repository.GeocodingRepository;
import rx.Observable;

/**
 * Created by Junya Ogasawara on 1/15/17.
 */

public class GeocodingUsingZipCode extends UseCase {

    private final GeocodingRepository geocodingRepository;

    private String zipCode;

    @Inject
    public GeocodingUsingZipCode(GeocodingRepository geocodingRepository,
                                 ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.geocodingRepository = geocodingRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return this.geocodingRepository.geocodingWithZipCode(zipCode);
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
}
