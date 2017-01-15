/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.domain.repository;

import io.github.a0gajun.weather.domain.model.WatchingLocation;
import rx.Observable;
import rx.Single;

/**
 * Created by Junya Ogasawara on 1/14/17.
 */

// TODO: Rename
public interface WatchingLocationRepository {
    Observable<WatchingLocation> registerOrUpdateWatchingLocation(final WatchingLocation watchingLocation);

    Observable<WatchingLocation> getAllWatchingLocations();

    /**
     * @param zipCode
     * @return Observable of removed entity's zipcode
     */
    Single<String> unregisterWatchingLocation(final String zipCode);
}
