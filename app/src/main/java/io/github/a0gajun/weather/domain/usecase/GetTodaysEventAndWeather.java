/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.domain.usecase;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import org.threeten.bp.Duration;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import io.github.a0gajun.weather.domain.executor.PostExecutionThread;
import io.github.a0gajun.weather.domain.executor.ThreadExecutor;
import io.github.a0gajun.weather.domain.model.TodaysEventAndWeather;
import io.github.a0gajun.weather.domain.repository.CalendarRepository;
import io.github.a0gajun.weather.domain.repository.WeatherRepository;
import io.github.a0gajun.weather.domain.util.ZipCodeUtil;
import rx.Observable;
import timber.log.Timber;

/**
 * Created by Junya Ogasawara on 1/16/17.
 */

public class GetTodaysEventAndWeather extends UseCase {

    private final Context context;
    private final CalendarRepository calendarRepository;
    private final WeatherRepository weatherRepository;

    @Inject
    public GetTodaysEventAndWeather(Context context,
                                    CalendarRepository calendarRepository,
                                    WeatherRepository weatherRepository,
                                    ThreadExecutor threadExecutor,
                                    PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.context = context;
        this.calendarRepository = calendarRepository;
        this.weatherRepository = weatherRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        // TODO: ロジックが適当なので直す
        return this.calendarRepository.getTodaysEvent()
                .map(calendarEvent -> {
                    TodaysEventAndWeather todaysEventAndWeather = new TodaysEventAndWeather();
                    todaysEventAndWeather.setCalendarEvent(calendarEvent);
                    return todaysEventAndWeather;
                })
                .flatMap(todaysEventAndWeather -> {
                    Address address = resolveAddressFromLocation(todaysEventAndWeather.getCalendarEvent().getLocation());
                    if (address != null && address.getPostalCode() != null && ZipCodeUtil.isValidZipCode(address.getPostalCode())) {
                        Timber.d(address.toString());
                        return this.weatherRepository.fiveDayForecast(address.getPostalCode())
                                .flatMap(e -> Observable.from(e.getForecastData()))
                                .filter(data -> {
                                    long minutes = Duration.between(data.getForecastAt(), todaysEventAndWeather.getCalendarEvent().getStartTime()).toMinutes();
                                    Timber.d(todaysEventAndWeather.getCalendarEvent().getTitle());
                                    Timber.d(String.valueOf(minutes));
                                    return minutes <= 90;
                                })
                                .limit(1)
                                .map(data -> {
                                    todaysEventAndWeather.setForecastData(data);
                                    return todaysEventAndWeather;
                                });
                    } else {
                        return Observable.just(todaysEventAndWeather);
                    }
                }).toList();
    }

    private Address resolveAddressFromLocation(String location) {
        Geocoder geocoder = new Geocoder(context, Locale.ENGLISH); // TODO: Support Japanese
        try {
            List<Address> addresses = geocoder.getFromLocationName(location, 1);
            return addresses.isEmpty() ? null : addresses.get(0);
        } catch (IOException e) {
            return null;
        }
    }
}
