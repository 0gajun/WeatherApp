/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.data.repository;

import javax.inject.Inject;

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

    @Inject
    public GeocodingRepositoryImpl(GeocodingDataStoreFactory geocodingDataStoreFactory, GoogleMapsGeocodingMapper googleMapsGeocodingMapper) {
        this.geocodingDataStoreFactory = geocodingDataStoreFactory;
        this.googleMapsGeocodingMapper = googleMapsGeocodingMapper;
    }

    @Override
    public Observable<GeocodingResult> geocodingWithZipCode(String zipCode) {
        return this.geocodingDataStoreFactory.create()
                .geocodingEntityOfZipCode(zipCode)
                .map(entity -> {
                    GeocodingResult result = this.googleMapsGeocodingMapper.transform(entity);
                    if (result == null) {
                        throw new ZipCodeNotResolvedException("Couldn't geocode using zip code(" + zipCode + ")");
                    }
                    return result;
                });
    }
}
