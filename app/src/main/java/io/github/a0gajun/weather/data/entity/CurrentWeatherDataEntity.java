/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.data.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.github.a0gajun.weather.data.entity.owp_common.Clouds;
import io.github.a0gajun.weather.data.entity.owp_common.Coordinate;
import io.github.a0gajun.weather.data.entity.owp_common.Main;
import io.github.a0gajun.weather.data.entity.owp_common.Rain;
import io.github.a0gajun.weather.data.entity.owp_common.Snow;
import io.github.a0gajun.weather.data.entity.owp_common.Sys;
import io.github.a0gajun.weather.data.entity.owp_common.Weather;
import io.github.a0gajun.weather.data.entity.owp_common.Wind;
import lombok.Getter;

/**
 * Entity class of Current Weather Data API (provided by OpenWeatherMap)
 * This api documentation is here -> http://openweathermap.org/current
 * <p>
 * Created by Junya Ogasawara on 1/9/17.
 */

public class CurrentWeatherDataEntity {
    /**
     * City ID
     */
    @Getter
    @SerializedName("id")
    private int cityId;

    /**
     * City Name
     */
    @Getter
    @SerializedName("name")
    private String cityName;

    /**
     * Time of data calculation, unix, UTC
     */
    @Getter
    @SerializedName("dt")
    private long calculatedAt;

    @Getter
    @SerializedName("coord")
    private Coordinate coordinate;

    @Getter
    @SerializedName("weather")
    private List<Weather> weathers;

    @Getter
    @SerializedName("main")
    private Main main;

    @Getter
    @SerializedName("wind")
    private Wind wind;

    @Getter
    @SerializedName("clouds")
    private Clouds clouds;

    @Getter
    @SerializedName("rain")
    private Rain rain;

    @Getter
    @SerializedName("snow")
    private Snow snow;

    @Getter
    @SerializedName("sys")
    private Sys sys;
}
