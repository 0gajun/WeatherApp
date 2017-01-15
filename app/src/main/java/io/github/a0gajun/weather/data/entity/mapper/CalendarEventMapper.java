/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.data.entity.mapper;

import javax.inject.Inject;

import io.github.a0gajun.weather.data.entity.CalendarEventEntity;
import io.github.a0gajun.weather.data.entity.mapper.util.MapperUtil;
import io.github.a0gajun.weather.domain.model.CalendarEvent;

/**
 * Created by Junya Ogasawara on 1/16/17.
 */

public class CalendarEventMapper {

    @Inject
    public CalendarEventMapper() {
    }

    public CalendarEvent transform(CalendarEventEntity entity) {
        if (entity == null) {
            return null;
        }
        CalendarEvent calendarEvent = new CalendarEvent();
        calendarEvent.setTitle(entity.getTitle());
        calendarEvent.setLocation(entity.getLocation());
        calendarEvent.setStartTime(MapperUtil.convertMillisUnixTimeIntoZonedDateTime(entity.getDtStart()));
        calendarEvent.setEndTime(MapperUtil.convertMillisUnixTimeIntoZonedDateTime(entity.getDtEnd()));
        return calendarEvent;
    }
}
