/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.data.net;

import io.github.a0gajun.weather.data.entity.GoogleMapsGeocodingEntity;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Retrofit interface definition for Google Maps Api.
 * (https://developers.google.com/maps/)
 * <p>
 * Created by Junya Ogasawara on 1/11/17.
 */

public interface GoogleMapsApi {
    /**
     * Google Maps Geocoding API
     * https://developers.google.com/maps/documentation/geocoding/intro
     *
     * @param zipCode search target location's zip code.
     * @return
     */
    @GET("maps/api/geocode/json")
    Observable<GoogleMapsGeocodingEntity> getGeocodingWithZipCode(@Query("address") String zipCode);
}
