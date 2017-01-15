/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.data.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Junya Ogasawara on 1/16/17.
 */

public class CalendarEventEntity {
    @Getter @Setter private long id;

    @Getter @Setter private long calendarId;

    @Getter @Setter private String title;

    @Getter @Setter private String location;

    @Getter @Setter private long dtStart;

    @Getter @Setter private long dtEnd;
}
