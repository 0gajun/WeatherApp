/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.data.entity.mapper;

import android.support.annotation.Nullable;
import android.support.v7.app.WindowDecorActionBar;

import org.threeten.bp.Instant;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneId;

import javax.inject.Inject;

import io.github.a0gajun.weather.data.entity.CurrentWeatherDataEntity;
import io.github.a0gajun.weather.data.entity.mapper.util.MapperUtil;
import io.github.a0gajun.weather.data.entity.owm_common.Main;
import io.github.a0gajun.weather.data.entity.owm_common.Weather;
import io.github.a0gajun.weather.data.entity.owm_common.Wind;
import io.github.a0gajun.weather.domain.model.CurrentWeather;
import timber.log.Timber;

/**
 * Mapper class transforming {@link CurrentWeatherDataEntity} into
 * {@link CurrentWeather}.
 * <p>
 * Created by Junya Ogasawara on 1/9/17.
 */

public class CurrentWeatherMapper {

    @Inject
    CurrentWeatherMapper() {
    }

    @Nullable
    public CurrentWeather transform(final CurrentWeatherDataEntity entity) {
        if (entity == null || entity.getWeathers().isEmpty()) {
            return null;
        }
        CurrentWeather model = new CurrentWeather();
        model.setCountryCode(entity.getSys().getCountryCode());
        model.setCountryName(MapperUtil.convertCountryCodeToCountryName(entity.getSys().getCountryCode()));
        model.setCityId(entity.getCityId());
        model.setCityName(entity.getCityName());
        model.setCalculatedAt(MapperUtil.convertUnixTimeIntoZonedDateTime(entity.getCalculatedAt()));

        final Main main = entity.getMain();
        model.setTemperature(main.getTemp());
        model.setPressure(main.getPressure());
        model.setHumidity(main.getHumidity());
        model.setSeaLevel(main.getSeaLevel());
        model.setGrandLevel(main.getGrndLevel());

        final Weather weather = entity.getWeathers().get(0);
        model.setWeather(weather.getMain());
        model.setWeatherDescription(weather.getDescription());
        model.setWeatherIconUrl(MapperUtil.convertWeatherIconIntoIconUrl(weather.getIcon()));
        model.setWeatherIconResId(MapperUtil.convertWeatherIconIntoIconResId(weather.getIcon()));

        final Wind wind = entity.getWind();
        model.setWindSpeed(wind.getSpeed());
        model.setWindDegree(wind.getDeg());

        model.setCloudiness(entity.getClouds().getAll());
        //TODO: Handling properly
        if (entity.getRain() != null) {
            model.setRainOfLast3Hour(entity.getRain().getThreeHour());
        }
        if (entity.getSnow() != null) {
            model.setSnowOfLast3Hour(entity.getSnow().getThreeHour());
        }

        return model;
    }
}
