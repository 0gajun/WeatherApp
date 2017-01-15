/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.data.repository;

import android.content.ContentResolver;
import android.content.Context;

import javax.inject.Inject;

import io.github.a0gajun.weather.data.entity.CalendarEntity;
import io.github.a0gajun.weather.data.entity.mapper.CalendarEventMapper;
import io.github.a0gajun.weather.data.repository.datasource.CalendarProviderDataStore;
import io.github.a0gajun.weather.domain.model.CalendarEvent;
import io.github.a0gajun.weather.domain.repository.CalendarRepository;
import rx.Observable;

/**
 * Created by Junya Ogasawara on 1/16/17.
 */

public class CalendarRepositoryImpl implements CalendarRepository {

    private final ContentResolver contentResolver;
    private final CalendarEventMapper calendarEventMapper;

    @Inject
    public CalendarRepositoryImpl(Context context, CalendarEventMapper calendarEventMapper) {
        this.contentResolver = context.getContentResolver();
        this.calendarEventMapper = calendarEventMapper;
    }

    @Override
    public Observable<CalendarEvent> getTodaysEvent() {
        final CalendarProviderDataStore dataStore = new CalendarProviderDataStore(this.contentResolver);

        return dataStore.getCalendars()
                .distinct(CalendarEntity::getId)
                .flatMap(calendarEntity -> dataStore.getTodaysEvents(calendarEntity.getId()))
                .map(this.calendarEventMapper::transform);
    }
}
