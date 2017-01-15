/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.data.repository.datasource;

import io.github.a0gajun.weather.data.entity.GoogleMapsGeocodingCacheEntity;
import io.github.a0gajun.weather.data.entity.OrmaDatabase;
import rx.Observable;

/**
 * Created by Junya Ogasawara on 1/16/17.
 */

public class DBGeocodingDataStore {

    private final OrmaDatabase ormaDatabase;

    public DBGeocodingDataStore(OrmaDatabase ormaDatabase) {
        this.ormaDatabase = ormaDatabase;
    }

    public Observable<GoogleMapsGeocodingCacheEntity> geocodingEntityOfZipCode(String zipCode) {
        return this.ormaDatabase.selectFromGoogleMapsGeocodingCacheEntity()
                .postalCodeEq(zipCode).limit(1).executeAsObservable();
    }
}
