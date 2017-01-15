/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.presentation.view;

import java.util.List;

import io.github.a0gajun.weather.domain.model.TodaysEventAndWeather;

/**
 * Created by Junya Ogasawara on 1/16/17.
 */

public interface TodaysEventForecastView {

    void requireCalendarPermission();

    void render(List<TodaysEventAndWeather> todaysEventAndWeathers);
}
