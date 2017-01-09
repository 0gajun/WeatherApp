/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.data.entity.owm_common;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;

/**
 * Open Weather Map's common data structure representing City.
 * <p>
 * Created by Junya Ogasawara on 1/9/17.
 */

public class City {
    @Getter
    @SerializedName("id")
    private int id;

    @Getter
    @SerializedName("name")
    private String name;

    @Getter
    @SerializedName("coord")
    private Coordinate coordinate;

    @Getter
    @SerializedName("country")
    private String countryCode;
}
