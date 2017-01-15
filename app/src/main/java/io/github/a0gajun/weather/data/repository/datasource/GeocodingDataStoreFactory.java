/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.data.repository.datasource;

import javax.inject.Inject;

import io.github.a0gajun.weather.data.entity.OrmaDatabase;
import io.github.a0gajun.weather.data.net.GoogleMapsApi;

/**
 * Created by Junya Ogasawara on 1/12/17.
 */

public class GeocodingDataStoreFactory {

    private final GoogleMapsApi googleMapsApi;
    private final OrmaDatabase ormaDatabase;

    @Inject
    public GeocodingDataStoreFactory(OrmaDatabase ormaDatabase, GoogleMapsApi googleMapsApi) {
        this.googleMapsApi = googleMapsApi;
        this.ormaDatabase = ormaDatabase;
    }

    public GeocodingDataStore createCloudDataStore() {
        return new CloudGeocodingDataStore(this.googleMapsApi);
    }

    public DBGeocodingDataStore createDBGeocodingDataStore() {
        return new DBGeocodingDataStore(this.ormaDatabase);
    }
}
