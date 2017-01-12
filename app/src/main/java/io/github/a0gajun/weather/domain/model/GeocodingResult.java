/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.domain.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Model representing geocoding result by google maps api.
 *
 * Created by Junya Ogasawara on 1/12/17.
 */

public class GeocodingResult {
    @Getter @Setter private String postalCode;
    @Getter @Setter private String countryName;
    @Getter @Setter private String countryCode;
    @Getter @Setter private String cityName;
}
