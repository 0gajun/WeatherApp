/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.data.repository.datasource;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.CalendarContract;

import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.temporal.ChronoUnit;

import io.github.a0gajun.weather.data.entity.CalendarEntity;
import io.github.a0gajun.weather.data.entity.CalendarEventEntity;
import rx.Observable;
import timber.log.Timber;

import static android.provider.CalendarContract.Calendars;
import static android.provider.CalendarContract.Events;

/**
 * Created by Junya Ogasawara on 1/16/17.
 */

public class CalendarProviderDataStore {

    private final ContentResolver contentResolver;

    public CalendarProviderDataStore(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    @SuppressWarnings("MissingPermission")
    public Observable<CalendarEventEntity> getTodaysEvents(long calenderId) {
        return Observable.create(subscriber -> {
            //TODO: Consider timezone
            final ZonedDateTime today = ZonedDateTime.now().truncatedTo(ChronoUnit.DAYS);
            final ZonedDateTime tomorrow = ZonedDateTime.ofInstant(today.toInstant(), today.getZone()).plusDays(1);

            final String[] projection = new String[]{Events._ID, Events.CALENDAR_ID, Events.TITLE,
                    Events.EVENT_LOCATION, Events.DTSTART, Events.DTEND};
            final String selection = "(" + Events.CALENDAR_ID + " = ? and ? < " + Events.DTSTART + " and " + Events.DTEND + " < ?)";
            final String[] selectionArgs = new String[]{
                    String.valueOf(calenderId),
                    String.valueOf(today.toInstant().toEpochMilli()),
                    String.valueOf(tomorrow.toInstant().toEpochMilli())};
            final Cursor cursor = this.contentResolver.query(Events.CONTENT_URI, projection, selection, selectionArgs, null);

            while (cursor.moveToNext()) {
                CalendarEventEntity entity = new CalendarEventEntity();
                entity.setId(cursor.getLong(0));
                entity.setCalendarId(cursor.getLong(1));
                entity.setTitle(cursor.getString(2));
                entity.setLocation(cursor.getString(3));
                entity.setDtStart(cursor.getLong(4));
                entity.setDtEnd(cursor.getLong(5));
                subscriber.onNext(entity);
            }
            cursor.close();
            subscriber.onCompleted();
        });
    }

    @SuppressWarnings("MissingPermission")
    public Observable<CalendarEntity> getCalendars() {
        return Observable.create(subscriber -> {
            final String[] projection = new String[]{Calendars._ID, Calendars.NAME};
            final Cursor cursor = this.contentResolver.query(CalendarContract.Calendars.CONTENT_URI, projection, null, null, null);

            while (cursor.moveToNext()) {
                CalendarEntity entity = new CalendarEntity();
                entity.setId(cursor.getLong(0));
                entity.setName(cursor.getString(1));
                subscriber.onNext(entity);
            }

            cursor.close();
            subscriber.onCompleted();
        });
    }

}
