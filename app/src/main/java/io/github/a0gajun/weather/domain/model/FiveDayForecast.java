/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.domain.model;

import android.support.annotation.DrawableRes;

import org.threeten.bp.ZonedDateTime;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Model representing 5 day forecast includes weather data every 3 hours.
 * <p>
 * Created by Junya Ogasawara on 1/9/17.
 */

public class FiveDayForecast {

    // city
    @Getter @Setter private int cityId;

    @Getter @Setter private String cityName;

    @Getter @Setter private String countryCode;

    @Getter @Setter private String countryName;

    @Getter @Setter private List<EveryThreeHoursForecastData> forecastData;

    public static class EveryThreeHoursForecastData {
        /**
         * Time of data forecasted
         */
        @Getter @Setter private ZonedDateTime forecastAt;

        // main
        @Getter @Setter private float temperature;

        @Getter @Setter private float pressure;

        @Getter @Setter private int humidity;

        @Getter @Setter private float seaLevel;

        @Getter @Setter private float grandLevel;

        // weather
        @Getter @Setter private String weather;

        @Getter @Setter private String weatherDescription;

        @Getter @Setter private String weatherIconUrl;

        @Getter @Setter @DrawableRes private int weatherIconResId;

        // wind
        @Getter @Setter private float windSpeed;

        @Getter @Setter private float windDegree;

        // clouds
        @Getter @Setter private int cloudiness;

        // rain
        @Getter @Setter private float rainOfLast3Hour;

        // ic_snow
        @Getter @Setter private float snowOfLast3Hour;

        /**
         * Data/time of caluclation
         */
        @Getter @Setter private ZonedDateTime calculatedAt;
    }
}
