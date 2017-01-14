/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.data.repository;

import javax.inject.Inject;

import io.github.a0gajun.weather.data.entity.OrmaDatabase;
import io.github.a0gajun.weather.data.entity.WatchingLocationEntity;
import io.github.a0gajun.weather.data.entity.mapper.WatchingLocationMapper;
import io.github.a0gajun.weather.domain.model.WatchingLocation;
import io.github.a0gajun.weather.domain.repository.WatchingLocationRepository;
import rx.Observable;

/**
 * Created by Junya Ogasawara on 1/14/17.
 */

public class WatchingLocationRepositoryImpl implements WatchingLocationRepository {

    private final OrmaDatabase ormaDatabase;
    private final WatchingLocationMapper watchingLocationMapper;

    @Inject
    public WatchingLocationRepositoryImpl(OrmaDatabase ormaDatabase,
                                          WatchingLocationMapper watchingLocationMapper) {
        this.ormaDatabase = ormaDatabase;
        this.watchingLocationMapper = watchingLocationMapper;
    }

    @Override
    public Observable<WatchingLocation> registerOrUpdateWatchingLocation(WatchingLocation watchingLocation) {
         WatchingLocationEntity entity = this.watchingLocationMapper.transform(watchingLocation);

        if (entity == null) {
            return Observable.error(new RuntimeException("")); // TODO: Throw proper exception
        }

        return Observable.create(subscriber -> this.ormaDatabase.insertIntoWatchingLocationEntity(entity));
    }

    @Override
    public Observable<WatchingLocation> getAllWatchingLocations() {
        return null;
    }
}
