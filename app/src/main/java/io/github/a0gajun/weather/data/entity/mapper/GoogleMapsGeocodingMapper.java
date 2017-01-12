/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.data.entity.mapper;

import android.support.annotation.Nullable;

import java.util.List;

import javax.inject.Inject;

import io.github.a0gajun.weather.data.entity.GoogleMapsGeocodingEntity;
import io.github.a0gajun.weather.domain.model.GeocodingResult;

/**
 * Mapper class transforming {@link GoogleMapsGeocodingEntity} into {@link GeocodingResult}
 *
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

        if (model.getCountryCode().isEmpty()
                || model.getCityName().isEmpty()
                || model.getPostalCode().isEmpty()) {
            return null;
        }

        return model;
    }
}
