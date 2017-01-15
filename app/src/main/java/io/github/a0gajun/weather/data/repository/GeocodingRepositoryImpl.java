/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.data.repository;

import javax.inject.Inject;

import io.github.a0gajun.weather.data.entity.OrmaDatabase;
import io.github.a0gajun.weather.data.entity.mapper.GoogleMapsGeocodingMapper;
import io.github.a0gajun.weather.data.exception.ZipCodeNotResolvedException;
import io.github.a0gajun.weather.data.repository.datasource.GeocodingDataStoreFactory;
import io.github.a0gajun.weather.domain.model.GeocodingResult;
import io.github.a0gajun.weather.domain.repository.GeocodingRepository;
import rx.Observable;

/**
 * Created by Junya Ogasawara on 1/12/17.
 */

public class GeocodingRepositoryImpl implements GeocodingRepository {

    final GeocodingDataStoreFactory geocodingDataStoreFactory;
    final GoogleMapsGeocodingMapper googleMapsGeocodingMapper;
    final OrmaDatabase ormaDatabase;

    @Inject
    public GeocodingRepositoryImpl(OrmaDatabase ormaDatabase, GeocodingDataStoreFactory geocodingDataStoreFactory, GoogleMapsGeocodingMapper googleMapsGeocodingMapper) {
        this.ormaDatabase = ormaDatabase;
        this.geocodingDataStoreFactory = geocodingDataStoreFactory;
        this.googleMapsGeocodingMapper = googleMapsGeocodingMapper;
    }

    @Override
    public Observable<GeocodingResult> geocodingWithZipCode(String zipCode) {
        Observable<GeocodingResult> cacheObservable = this.geocodingDataStoreFactory.createDBGeocodingDataStore()
                .geocodingEntityOfZipCode(zipCode)
                .map(this.googleMapsGeocodingMapper::transform);

        Observable<GeocodingResult> cloudObservable = this.geocodingDataStoreFactory.createCloudDataStore()
                .geocodingEntityOfZipCode(zipCode)
                .map(this.googleMapsGeocodingMapper::transform)
                .doOnNext(geocodingResult -> {
                    if (geocodingResult == null) {
                        throw new ZipCodeNotResolvedException("Couldn't geocode using zip code(" + zipCode + ")");
                    }
                })
                .doOnNext(this::insertCache);

        return cacheObservable.switchIfEmpty(cloudObservable);
    }

    //TODO: Consider responsibility of Repository
    //TODO: Consider expiration
    private void insertCache(GeocodingResult geocodingResult) {
        this.ormaDatabase.insertIntoGoogleMapsGeocodingCacheEntity(this.googleMapsGeocodingMapper.transformModelIntoCache(geocodingResult));
    }
}
