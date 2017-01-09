/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.data.entity.owp_common;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;

/**
 * Open Weather Map's common data structure representing Sys (Unknown).
 * <p>
 * Created by Junya Ogasawara on 1/9/17.
 */

public class Sys {

    @Getter
    @SerializedName("country")
    private String countryCode;

    @Getter
    @SerializedName("sunrise")
    private long sunriseTime;

    @Getter
    @SerializedName("sunset")
    private long sunsetTime;
}
