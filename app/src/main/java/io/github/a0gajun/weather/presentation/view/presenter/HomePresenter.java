/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.presentation.view.presenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.github.a0gajun.weather.domain.model.CurrentWeatherAndForecast;
import io.github.a0gajun.weather.domain.model.WatchingLocation;
import io.github.a0gajun.weather.domain.usecase.DefaultSubscriber;
import io.github.a0gajun.weather.domain.usecase.RegisterWatchingLocation;
import io.github.a0gajun.weather.domain.usecase.UseCase;
import io.github.a0gajun.weather.presentation.di.module.Qualifiers;
import io.github.a0gajun.weather.presentation.view.HomeView;
import io.github.a0gajun.weather.presentation.view.LoadingView;
import timber.log.Timber;

/**
 * Created by Junya Ogasawara on 1/14/17.
 */

public class HomePresenter implements Presenter {

    private final UseCase getCurrentLocationWeatherAndForecast;
    private final UseCase getRegisteredLocationWeatherAndForecast;
    private final RegisterWatchingLocation registerWatchingLocation;

    private HomeView homeView;
    private LoadingView loadingView;
    private List<CurrentWeatherAndForecast> weathers = new ArrayList<>();
    private CurrentWeatherAndForecast currentLocationWeatherAndForecast;

    @Inject
    public HomePresenter(@Named(Qualifiers.CURRENT_LOCATION_WEATHER_FORECAST) UseCase getCurrentLocationWeatherAndForecast,
                         @Named(Qualifiers.REGISTERED_LOCATION_WEATHER_FORECAST) UseCase getRegisteredLocationWeatherAndForecast,
                         @Named(Qualifiers.REGISTER_WATCHING_LOCATION) RegisterWatchingLocation registerWatchingLocation) {
        this.getCurrentLocationWeatherAndForecast = getCurrentLocationWeatherAndForecast;
        this.getRegisteredLocationWeatherAndForecast = getRegisteredLocationWeatherAndForecast;
        this.registerWatchingLocation = registerWatchingLocation;
    }

    public <T extends HomeView & LoadingView> void setView(T view) {
        this.homeView = view;
        this.loadingView = view;
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        this.homeView = null;
        this.weathers = null;

        this.getCurrentLocationWeatherAndForecast.unsubscribe();
        this.getRegisteredLocationWeatherAndForecast.unsubscribe();
    }

    private void render() {
        this.homeView.renderWeathers(buildRenderingTargetData());
    }

    private List<CurrentWeatherAndForecast> buildRenderingTargetData() {
        List<CurrentWeatherAndForecast> newList = new ArrayList<>(this.weathers);
        if (this.currentLocationWeatherAndForecast != null) {
            newList.add(0, this.currentLocationWeatherAndForecast);
        }

        return newList;
    }

    public void registerWatchingLocation(final String zipCode) {
        WatchingLocation watchingLocation = new WatchingLocation();
        watchingLocation.setPriority(0);
        watchingLocation.setZipCode(zipCode);
        this.registerWatchingLocation.setWatchingLocation(watchingLocation);
        this.registerWatchingLocation.execute(new RegisterWatchingLocationSubscriber());
    }

    public void loadRegisteredWeathers() {
        initializeState();
        this.getRegisteredLocationWeatherAndForecast.unsubscribe();
        this.getRegisteredLocationWeatherAndForecast.execute(new LoadRegisteredWeathersSubscriber());
    }

    public void loadCurrentLocationWeather() {
        this.homeView.requireLocationPermission();
    }

    /**
     * This method assumes that this application already got any of
     * Manifest.permission.ACCESS_COARSE_LOCATION and Manifest.permission.ACCESS_FILE_LOCATION permission.
     */
    public void loadCurrentLocationWeatherWithProperPermission() {
        this.getCurrentLocationWeatherAndForecast.unsubscribe();
        this.getCurrentLocationWeatherAndForecast.execute(new LoadCurrentLocationWeatherSubscriber());
    }

    public void reload() {
        loadRegisteredWeathers();
        loadCurrentLocationWeather();
    }

    private void initializeState() {
        this.weathers.clear();
    }

    private void updateWeathersList(List<CurrentWeatherAndForecast> currentWeatherAndForecast) {
        this.weathers = currentWeatherAndForecast;
    }

    private void updateCurrentLocationWeather(CurrentWeatherAndForecast currentWeatherAndForecast) {
        this.currentLocationWeatherAndForecast = currentWeatherAndForecast;
    }

    private class LoadRegisteredWeathersSubscriber extends DefaultSubscriber<List<CurrentWeatherAndForecast>> {

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            Timber.e(e);
        }

        @Override
        public void onNext(List<CurrentWeatherAndForecast> currentWeatherAndForecastList) {
            HomePresenter.this.updateWeathersList(currentWeatherAndForecastList);
            HomePresenter.this.render();
        }
    }

    private class LoadCurrentLocationWeatherSubscriber extends DefaultSubscriber<CurrentWeatherAndForecast> {
        @Override
        public void onError(Throwable e) {
            super.onError(e);
            Timber.e(e);
        }

        @Override
        public void onNext(CurrentWeatherAndForecast currentWeatherAndForecast) {
            HomePresenter.this.updateCurrentLocationWeather(currentWeatherAndForecast);
            HomePresenter.this.render();
        }
    }

    private class RegisterWatchingLocationSubscriber extends DefaultSubscriber<WatchingLocation> {
        @Override
        public void onCompleted() {
            super.onCompleted();
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
        }

        @Override
        public void onNext(WatchingLocation watchingLocation) {
            super.onNext(watchingLocation);
        }
    }
}
