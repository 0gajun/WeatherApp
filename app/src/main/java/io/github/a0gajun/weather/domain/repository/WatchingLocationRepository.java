/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.domain.repository;

import io.github.a0gajun.weather.domain.model.WatchingLocation;
import rx.Observable;

/**
 * Created by Junya Ogasawara on 1/14/17.
 */

// TODO: Rename
public interface WatchingLocationRepository {
    Observable<WatchingLocation> registerOrUpdateWatchingLocation(final WatchingLocation watchingLocation);

    Observable<WatchingLocation> getAllWatchingLocations();
}
