/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.data.entity.mapper;

import android.support.annotation.Nullable;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZoneOffset;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import javax.inject.Inject;

import io.github.a0gajun.weather.data.entity.FiveDayWeatherForecastDataEntity;
import io.github.a0gajun.weather.data.entity.mapper.util.MapperUtil;
import io.github.a0gajun.weather.data.entity.owm_common.City;
import io.github.a0gajun.weather.data.entity.owm_common.Main;
import io.github.a0gajun.weather.data.entity.owm_common.Weather;
import io.github.a0gajun.weather.data.entity.owm_common.Wind;
import io.github.a0gajun.weather.domain.model.FiveDayForecast;

/**
 * Mapper class transforming {@link FiveDayWeatherForecastDataEntity} into
 * {@link FiveDayForecast}.
 * <p>
 * Created by Junya Ogasawara on 1/9/17.
 */

public class FiveDayForecastMapper {

    private static final DateTimeFormatter CALCULATED_AT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Inject
    FiveDayForecastMapper() {
    }

    @Nullable
    public FiveDayForecast transform(final FiveDayWeatherForecastDataEntity entity) {
        if (entity == null) {
            return null;
        }
        FiveDayForecast model = new FiveDayForecast();

        final City city = entity.getCity();
        model.setCityId(city.getId());
        model.setCityName(city.getName());
        model.setCountryCode(city.getCountryCode());
        model.setCountryName(MapperUtil.convertCountryCodeToCountryName(city.getCountryCode()));

        model.setForecastData(
                Stream.of(entity.getList())
                        .map(this::transformForecast)
                        .filter(value -> value != null)
                        .collect(Collectors.toList())
        );
        return model;
    }

    @Nullable
    private FiveDayForecast.EveryThreeHoursForecastData transformForecast(final FiveDayWeatherForecastDataEntity.Forecast forecast) {
        if (forecast.getWeathers().isEmpty()) {
            return null;
        }
        FiveDayForecast.EveryThreeHoursForecastData data = new FiveDayForecast.EveryThreeHoursForecastData();

        data.setForecastAt(MapperUtil.convertUnixTimeIntoZonedDateTime(forecast.getTime()));
        LocalDateTime ldt = LocalDateTime.parse(forecast.getCalculatedAt(), CALCULATED_AT_FORMATTER);
        data.setForecastAt(ZonedDateTime.ofInstant(ldt.toInstant(ZoneOffset.UTC), ZoneId.systemDefault()));

        final Main main = forecast.getMain();
        data.setTemperature(main.getTemp());
        data.setPressure(main.getPressure());
        data.setHumidity(main.getHumidity());
        data.setSeaLevel(main.getSeaLevel());
        data.setGrandLevel(main.getGrndLevel());

        final Weather weather = forecast.getWeathers().get(0);
        data.setWeather(weather.getMain());
        data.setWeatherDescription(weather.getDescription());
        data.setWeatherIconUrl(MapperUtil.convertWeatherIconIntoIconUrl(weather.getIcon()));
        data.setWeatherIconResId(MapperUtil.convertWeatherIconIntoIconResId(weather.getIcon()));

        final Wind wind = forecast.getWind();
        data.setWindSpeed(wind.getSpeed());
        data.setWindDegree(wind.getDeg());

        data.setCloudiness(forecast.getClouds().getAll());

        //TODO: Handling properly
        if (forecast.getRain() != null) {
            data.setRainOfLast3Hour(forecast.getRain().getThreeHour());
        }
        if (forecast.getSnow() != null) {
            data.setSnowOfLast3Hour(forecast.getSnow().getThreeHour());
        }

        return data;
    }
}
