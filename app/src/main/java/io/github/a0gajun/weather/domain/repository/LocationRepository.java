/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.domain.repository;


import android.location.Location;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;

import rx.Observable;

/**
 * Created by Junya Ogasawara on 1/12/17.
 */

public interface LocationRepository {

    @RequiresPermission(
        anyOf = {"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"}
    )
    Observable<Location> startLocationUpdates();

    void stopLocationUpdates();

    @Nullable
    Location getCurrentLocation();

}
