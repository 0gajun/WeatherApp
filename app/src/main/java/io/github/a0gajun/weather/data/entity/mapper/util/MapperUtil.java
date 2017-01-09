/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.data.entity.mapper.util;

import org.threeten.bp.Instant;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;

/**
 * Created by Junya Ogasawara on 1/9/17.
 */

public class MapperUtil {

    public static ZonedDateTime convertUnixTimeIntoZonedDateTime(final long unixTime) {
        final Instant instant = Instant.ofEpochMilli(unixTime);
        return ZonedDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    // TODO: Move to appropriate class
    public static String convertWeatherIconIntoIconUrl(final String iconOfWeather) {
        return "http://openweathermap.org/img/w/" + iconOfWeather + ".png";
    }
}
