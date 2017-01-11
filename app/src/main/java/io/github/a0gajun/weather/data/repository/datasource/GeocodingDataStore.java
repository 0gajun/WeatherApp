/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.data.repository.datasource;

import io.github.a0gajun.weather.data.entity.GoogleMapsGeocodingEntity;
import rx.Observable;

/**
 * Created by Junya Ogasawara on 1/12/17.
 */

public interface GeocodingDataStore {

    Observable<GoogleMapsGeocodingEntity> geocodingEntityOfZipCode(final String zipCode);

}
