/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.data.entity.mapper;

import javax.inject.Inject;

import io.github.a0gajun.weather.data.entity.FiveDayWeatherForecastDataEntity;
import io.github.a0gajun.weather.domain.model.FiveDayForecast;

/**
 * Created by Junya Ogasawara on 1/9/17.
 */

public class FiveDayForecastMapper {

    @Inject
    public FiveDayForecastMapper() {
    }

    public FiveDayForecast transform(final FiveDayWeatherForecastDataEntity entity) {
        return new FiveDayForecast();
    }
}
