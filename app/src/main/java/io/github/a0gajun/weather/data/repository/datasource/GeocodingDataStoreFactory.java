/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.data.repository.datasource;

import javax.inject.Inject;

import io.github.a0gajun.weather.data.net.GoogleMapsApi;

/**
 * Created by Junya Ogasawara on 1/12/17.
 */

public class GeocodingDataStoreFactory {

    private final GoogleMapsApi googleMapsApi;

    @Inject
    public GeocodingDataStoreFactory(GoogleMapsApi googleMapsApi) {
        this.googleMapsApi = googleMapsApi;
    }

    public GeocodingDataStore create() {
        // TODO: Implement caching
        return createCloudDataStore();
    }

    private GeocodingDataStore createCloudDataStore() {
        return new CloudGeocodingDataStore(this.googleMapsApi);
    }
}
