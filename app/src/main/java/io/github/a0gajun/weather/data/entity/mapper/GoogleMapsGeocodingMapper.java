/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.data.entity.mapper;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.List;

import javax.inject.Inject;

import io.github.a0gajun.weather.data.entity.GoogleMapsGeocodingCacheEntity;
import io.github.a0gajun.weather.data.entity.GoogleMapsGeocodingEntity;
import io.github.a0gajun.weather.domain.model.GeocodingResult;

/**
 * Mapper class transforming {@link GoogleMapsGeocodingEntity} into {@link GeocodingResult}
 * <p>
 * Created by Junya Ogasawara on 1/12/17.
 */

public class GoogleMapsGeocodingMapper {

    @Inject
    public GoogleMapsGeocodingMapper() {
    }

    @Nullable
    public GeocodingResult transform(GoogleMapsGeocodingEntity entity) {
        if (entity == null || entity.getResults().size() != 1) {
            // if size isn't zero, there are too many candidates. So, return an error.
            return null;
        }
        GeocodingResult model = new GeocodingResult();
        final GoogleMapsGeocodingEntity.Result result = entity.getResults().get(0);
        for (GoogleMapsGeocodingEntity.AddressComponent ac : result.getAddressComponents()) {
            final List<String> types = ac.getTypes();
            if (types.contains("country")) { // Country
                model.setCountryCode(ac.getShortName());
                model.setCountryName(ac.getLongName());
            }
            if (types.contains("locality")) { // City
                model.setCityName(ac.getShortName());
            }
            if (types.contains("postal_code")) { // Postal code
                model.setPostalCode(ac.getLongName());
            }
        }

        if (TextUtils.isEmpty(model.getCountryCode())
                || TextUtils.isEmpty(model.getCityName())
                || TextUtils.isEmpty(model.getPostalCode())) {
            return null;
        }

        return model;
    }

    public GoogleMapsGeocodingCacheEntity transformModelIntoCache(GeocodingResult model) {
        GoogleMapsGeocodingCacheEntity cache = new GoogleMapsGeocodingCacheEntity();

        cache.setPostalCode(model.getPostalCode());
        cache.setCountryCode(model.getCountryCode());
        cache.setCountryName(model.getCountryName());
        cache.setCityName(model.getCityName());

        return cache;
    }

    public GeocodingResult transform(GoogleMapsGeocodingCacheEntity cache) {
        GeocodingResult result = new GeocodingResult();

        result.setPostalCode(cache.getPostalCode());
        result.setCityName(cache.getCityName());
        result.setCountryName(cache.getCountryName());
        result.setCountryCode(cache.getCountryCode());

        return result;
    }
}
