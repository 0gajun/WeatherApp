/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.domain.usecase;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.Nullable;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import io.github.a0gajun.weather.domain.executor.PostExecutionThread;
import io.github.a0gajun.weather.domain.executor.ThreadExecutor;
import io.github.a0gajun.weather.domain.model.CurrentWeather;
import io.github.a0gajun.weather.domain.model.CurrentWeatherAndForecast;
import io.github.a0gajun.weather.domain.model.FiveDayForecast;
import io.github.a0gajun.weather.domain.repository.LocationRepository;
import io.github.a0gajun.weather.domain.repository.WeatherRepository;
import rx.Observable;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by Junya Ogasawara on 1/14/17.
 */

public class GetCurrentLocationWeatherAndForecast extends UseCase {

    private final Context context;
    private final LocationRepository locationRepository;
    private final WeatherRepository weatherRepository;

    @Inject
    public GetCurrentLocationWeatherAndForecast(Context context,
                                                ThreadExecutor threadExecutor,
                                                PostExecutionThread postExecutionThread,
                                                LocationRepository locationRepository,
                                                WeatherRepository weatherRepository) {
        super(threadExecutor, postExecutionThread);
        this.context = context;
        this.locationRepository = locationRepository;
        this.weatherRepository = weatherRepository;
    }

    @Override
    @SuppressWarnings("MissingPermission")
    protected Observable buildUseCaseObservable() {
        return this.locationRepository.startLocationUpdates()
                .observeOn(Schedulers.from(this.threadExecutor))
                .flatMap(location -> {
                    final Address address = this.resolveAddressFromLocation(location);
                    final String zipCode = address.getPostalCode();
                    Observable<CurrentWeather> currentWeatherObservable = this.weatherRepository.currentWeather(zipCode);
                    Observable<FiveDayForecast> fiveDayForecastObservable = this.weatherRepository.fiveDayForecast(zipCode);

                    return Observable.zip(currentWeatherObservable,
                            fiveDayForecastObservable,
                            ((currentWeather, forecast) -> new CurrentWeatherAndForecast(currentWeather, forecast, zipCode)));
                })
                .map(currentWeatherAndForecast -> {
                    Timber.d(currentWeatherAndForecast.toString());
                    currentWeatherAndForecast.getCurrentWeather().setWeather("Current");
                    currentWeatherAndForecast.setCurrentLocation(true);
                    return currentWeatherAndForecast;
                });
    }

    @Nullable
    private Address resolveAddressFromLocation(Location location) {
        Timber.d("resolveAddressFromLocation");
        Geocoder geocoder = new Geocoder(this.context, Locale.ENGLISH);
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (!addresses.isEmpty()) {
                return addresses.get(0);
            }
        } catch (IOException ignored) {
        }

        throw new RuntimeException("Couldn't resolved Address from Location");
    }

    @Override
    public void unsubscribe() {
        this.locationRepository.stopLocationUpdates();
        super.unsubscribe();
    }
}
