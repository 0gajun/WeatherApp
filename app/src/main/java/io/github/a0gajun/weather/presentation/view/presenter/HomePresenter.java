/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.presentation.view.presenter;

import android.support.v7.app.WindowDecorActionBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import io.github.a0gajun.weather.domain.model.CurrentWeatherAndForecast;
import io.github.a0gajun.weather.domain.usecase.DefaultSubscriber;
import io.github.a0gajun.weather.domain.usecase.GetCurrentWeatherAndForecast;
import io.github.a0gajun.weather.presentation.view.HomeView;
import io.github.a0gajun.weather.presentation.view.LoadingView;
import timber.log.Timber;

import static io.github.a0gajun.weather.presentation.di.module.Qualifiers.GET_CURRENT_WEATHER_AND_FORECAST;

/**
 * Created by Junya Ogasawara on 1/14/17.
 */

public class HomePresenter implements Presenter {

    private final GetCurrentWeatherAndForecast getCurrentWeatherAndForecast;

    private HomeView homeView;
    private LoadingView loadingView;
    private List<CurrentWeatherAndForecast> weathers = new ArrayList<>();
    private Set<String> loadedZipCodes = new HashSet<>();

    @Inject
    public HomePresenter(@Named(GET_CURRENT_WEATHER_AND_FORECAST) GetCurrentWeatherAndForecast getCurrentWeatherAndForecast) {
        this.getCurrentWeatherAndForecast = getCurrentWeatherAndForecast;
    }

    public void setView(HomeView homeView, LoadingView loadingView) {
        this.homeView = homeView;
        this.loadingView = loadingView;
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
        this.loadedZipCodes = null;

        this.getCurrentWeatherAndForecast.unsubscribe();
    }

    private void render() {
        this.homeView.renderWeathers(this.weathers);
    }

    public void loadWeathers() {
        initializeState();
        // TODO: Fix proper value
        this.loadWeather("223-0061");
        this.loadWeather("150-0013");
        this.loadWeather("192-0353");
        this.loadWeather("206-0012");
    }

    private void initializeState() {
        this.weathers.clear();
        this.loadedZipCodes.clear();
    }

    private void loadWeather(final String zipCode) {
        this.getCurrentWeatherAndForecast.setZipCode(zipCode);
        this.getCurrentWeatherAndForecast.execute(new LoadWeatherSubscriber(zipCode));
    }

    private synchronized void updateWeathersList(final String zipCode, CurrentWeatherAndForecast currentWeatherAndForecast) {
        if (loadedZipCodes.contains(zipCode)) {
            return;
        }
        this.loadedZipCodes.add(zipCode);
        this.weathers.add(currentWeatherAndForecast);
    }

    private class LoadWeatherSubscriber extends DefaultSubscriber<CurrentWeatherAndForecast> {
        final String zipCode;

        public LoadWeatherSubscriber(String zipCode) {
            this.zipCode = zipCode;
        }

        @Override
        public void onCompleted() {
            super.onCompleted();
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
        }

        @Override
        public void onNext(CurrentWeatherAndForecast currentWeatherAndForecast) {
            HomePresenter.this.updateWeathersList(this.zipCode, currentWeatherAndForecast);
            HomePresenter.this.render();
        }
    }
}
