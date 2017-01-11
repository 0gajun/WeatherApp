/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.data.entity.owm_common;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;

/**
 * Open Weather Map's common data structure representing Main information.
 * (temperature, pressure, humidity and so on...)
 * <p>
 * Created by Junya Ogasawara on 1/9/17.
 */

public class Main {

    @Getter
    @SerializedName("temp")
    private float temp;

    @Getter
    @SerializedName("pressure")
    private float pressure;

    @Getter
    @SerializedName("humidity")
    private int humidity;

    @Getter
    @SerializedName("temp_min")
    private float tempMin;

    @Getter
    @SerializedName("temp_max")
    private float tempMax;

    @Getter
    @SerializedName("sea_level")
    private float seaLevel;

    @Getter
    @SerializedName("grnd_level")
    private float grndLevel;
}
