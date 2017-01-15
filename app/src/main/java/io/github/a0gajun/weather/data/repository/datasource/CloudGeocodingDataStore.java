/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.data.repository.datasource;

import io.github.a0gajun.weather.data.entity.GoogleMapsGeocodingEntity;
import io.github.a0gajun.weather.data.net.GoogleMapsApi;
import rx.Observable;

/**
 * Created by Junya Ogasawara on 1/12/17.
 */

class CloudGeocodingDataStore implements GeocodingDataStore {

    private final GoogleMapsApi googleMapsApi;

    CloudGeocodingDataStore(GoogleMapsApi googleMapsApi) {
        this.googleMapsApi = googleMapsApi;
    }

    @Override
    public Observable<GoogleMapsGeocodingEntity> geocodingEntityOfZipCode(String zipCode) {
        return this.googleMapsApi.getGeocodingWithZipCode(zipCode);
    }
}
