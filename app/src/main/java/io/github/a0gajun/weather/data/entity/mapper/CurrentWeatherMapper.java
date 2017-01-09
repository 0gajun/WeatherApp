/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.data.entity.mapper;

import javax.inject.Inject;

import io.github.a0gajun.weather.data.entity.CurrentWeatherDataEntity;
import io.github.a0gajun.weather.domain.model.CurrentWeather;

/**
 * Created by Junya Ogasawara on 1/9/17.
 */

public class CurrentWeatherMapper {

    @Inject
    CurrentWeatherMapper() {
    }

    public CurrentWeather transform(final CurrentWeatherDataEntity entity) {
        // TODO: Implement
        return new CurrentWeather();
    }

}
