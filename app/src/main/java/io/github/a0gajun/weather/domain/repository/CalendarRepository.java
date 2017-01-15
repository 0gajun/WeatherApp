/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.domain.repository;

import io.github.a0gajun.weather.domain.model.CalendarEvent;
import rx.Observable;

/**
 * Created by Junya Ogasawara on 1/16/17.
 */

public interface CalendarRepository {
    Observable<CalendarEvent> getTodaysEvent();
}
