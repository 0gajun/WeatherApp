/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.domain.repository;

import io.github.a0gajun.weather.domain.model.GeocodingResult;
import rx.Observable;

/**
 * Created by Junya Ogasawara on 1/12/17.
 */

public interface GeocodingRepository {
    Observable<GeocodingResult> geocodingWithZipCode(final String zipCode);
}
