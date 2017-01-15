/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.data.entity;

import com.github.gfx.android.orma.annotation.Column;
import com.github.gfx.android.orma.annotation.PrimaryKey;
import com.github.gfx.android.orma.annotation.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Junya Ogasawara on 1/16/17.
 */

@Table
public class GoogleMapsGeocodingCacheEntity {
    @PrimaryKey
    @Getter @Setter long id;

    @Column(unique = true, indexed = true)
    @Getter @Setter String postalCode;

    @Column
    @Getter @Setter String countryName;

    @Column
    @Getter @Setter String countryCode;

    @Column
    @Getter @Setter String cityName;
}
