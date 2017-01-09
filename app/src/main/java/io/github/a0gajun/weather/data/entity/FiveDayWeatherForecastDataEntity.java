/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.data.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.github.a0gajun.weather.data.entity.owm_common.City;
import io.github.a0gajun.weather.data.entity.owm_common.Clouds;
import io.github.a0gajun.weather.data.entity.owm_common.Coordinate;
import io.github.a0gajun.weather.data.entity.owm_common.Main;
import io.github.a0gajun.weather.data.entity.owm_common.Rain;
import io.github.a0gajun.weather.data.entity.owm_common.Snow;
import io.github.a0gajun.weather.data.entity.owm_common.Weather;
import io.github.a0gajun.weather.data.entity.owm_common.Wind;
import lombok.Getter;

/**
 * Entity class of 5day weather forecast API (provided by OpenWeatherMap)
 * This api documentation is here -> http://openweathermap.org/forecast5
 *
 * Created by Junya Ogasawara on 1/9/17.
 */

public class FiveDayWeatherForecastDataEntity {

    @Getter
    @SerializedName("city")
    private City city;

    @Getter
    @SerializedName("list")
    private List<Forecast> list;

    /**
     * Every 3hour's forecast data class.
     */
    public static class Forecast {
        /**
         * Time of data forecasted, unix, UTC
         */
        @Getter
        @SerializedName("dt")
        private long time;

        @Getter
        @SerializedName("main")
        private Main main;

        @Getter
        @SerializedName("weather")
        private Weather weather;

        @Getter
        @SerializedName("clouds")
        private Clouds clouds;

        @Getter
        @SerializedName("wind")
        private Wind wind;

        @Getter
        @SerializedName("rain")
        private Rain rain;

        @Getter
        @SerializedName("snow")
        private Snow snow;

        /**
         *  Data/time of caluclation, UTC
         */
        @Getter
        @SerializedName("dt_txt")
        private String calculatedAt;
    }
}
