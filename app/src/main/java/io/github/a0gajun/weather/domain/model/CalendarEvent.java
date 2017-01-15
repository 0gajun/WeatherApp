/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.domain.model;

import org.threeten.bp.ZonedDateTime;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Junya Ogasawara on 1/16/17.
 */

public class CalendarEvent {
    @Getter @Setter private String title;
    @Getter @Setter private String location;
    @Getter @Setter private ZonedDateTime startTime;
    @Getter @Setter private ZonedDateTime endTime;
}
