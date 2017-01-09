/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.data.entity.owp_common;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;

/**
 * Open Weather Map's common data structure representing Coordinate.
 * <p>
 * Created by Junya Ogasawara on 1/9/17.
 */

public class Coordinate {
    @Getter
    @SerializedName("lon")
    private float longitude;

    @Getter
    @SerializedName("lat")
    private float latitude;
}
