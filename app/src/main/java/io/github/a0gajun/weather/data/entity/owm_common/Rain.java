/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.data.entity.owm_common;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;

/**
 * Open Weather Map's common data structure representing Rain volume for the last 3 hours.
 * <p>
 * Created by Junya Ogasawara on 1/9/17.
 */

public class Rain {

    /**
     * Rain volume for the last 3 hours
     */
    @Getter
    @SerializedName("3h")
    private float threeHour;
}
