/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.data.entity.mapper.util;

import android.support.annotation.DrawableRes;
import android.support.annotation.IntegerRes;

import org.threeten.bp.Instant;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;

import java.security.PublicKey;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import io.github.a0gajun.weather.R;

/**
 * Created by Junya Ogasawara on 1/9/17.
 */

public class MapperUtil {

    /**
     * Mapping object representing http://openweathermap.org/weather-conditions
     */
    private static final Map<String, Integer> ICON_MAP = new HashMap<String, Integer>() {{
        put("01d", R.drawable.ic_day_clear_sky);
        put("02d", R.drawable.ic_day_few_clouds);
        put("03d", R.drawable.ic_clouds);
        put("04d", R.drawable.ic_broken_clouds);
        put("09d", R.drawable.ic_shower_rain);
        put("10d", R.drawable.ic_day_rain);
        put("11d", R.drawable.ic_thunderstorm);
        put("13d", R.drawable.ic_snow);
        put("50d", R.drawable.ic_mist);
        put("01n", R.drawable.ic_night_moon);
        put("02n", R.drawable.ic_night_cloud);
        put("03n", R.drawable.ic_clouds);
        put("04n", R.drawable.ic_broken_clouds);
        put("09n", R.drawable.ic_shower_rain);
        put("10n", R.drawable.ic_night_rain);
        put("11n", R.drawable.ic_thunderstorm);
        put("13n", R.drawable.ic_snow);
        put("50n", R.drawable.ic_mist);
    }};

    public static ZonedDateTime convertUnixTimeIntoZonedDateTime(final long unixTime) {
        final Instant instant = Instant.ofEpochMilli(unixTime);
        return ZonedDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    // TODO: Move to appropriate class
    public static String convertWeatherIconIntoIconUrl(final String iconOfWeather) {
        return "http://openweathermap.org/img/w/" + iconOfWeather + ".png";
    }

    public static int convertWeatherIconIntoIconResId(final String iconOfWeather) {
        Integer result = ICON_MAP.get(iconOfWeather);
        return (result != null) ? result : R.drawable.ic_question;
    }

    public static String convertCountryCodeToCountryName(final String countryCode) {
        Locale locale = new Locale("", countryCode);
        return locale.getDisplayCountry();
    }
}
