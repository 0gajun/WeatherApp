/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.presentation.view.presenter;

import javax.inject.Inject;

import io.github.a0gajun.weather.domain.model.CurrentWeather;
import io.github.a0gajun.weather.domain.model.CurrentWeatherAndForecast;
import io.github.a0gajun.weather.domain.model.GeocodingResult;
import io.github.a0gajun.weather.domain.usecase.DefaultSubscriber;
import io.github.a0gajun.weather.domain.usecase.GeocodingUsingZipCode;
import io.github.a0gajun.weather.domain.usecase.GetCurrentWeatherAndForecast;
import io.github.a0gajun.weather.presentation.view.LoadingView;
import io.github.a0gajun.weather.presentation.view.WeatherDetailView;
import timber.log.Timber;

/**
 * Created by Junya Ogasawara on 1/15/17.
 */

public class WeatherDetailPresenter implements Presenter {

    private final GetCurrentWeatherAndForecast getCurrentWeatherAndForecast;
    private final GeocodingUsingZipCode geocodingUsingZipCode;

    private WeatherDetailView weatherDetailView;
    private LoadingView loadingView;

    @Inject
    public WeatherDetailPresenter(GetCurrentWeatherAndForecast getCurrentWeatherAndForecast,
                                  GeocodingUsingZipCode geocodingUsingZipCode) {
        this.getCurrentWeatherAndForecast = getCurrentWeatherAndForecast;
        this.geocodingUsingZipCode = geocodingUsingZipCode;
    }

    public <T extends WeatherDetailView & LoadingView> void setView(T view) {
        this.weatherDetailView = view;
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
        this.getCurrentWeatherAndForecast.unsubscribe();
        this.geocodingUsingZipCode.unsubscribe();

        this.weatherDetailView = null;
        this.loadingView = null;
    }

    public void loadData(final String zipCode) {
        this.loadingView.showLoading();

        this.getCurrentWeatherAndForecast.setZipCode(zipCode);
        this.getCurrentWeatherAndForecast.execute(new CurrentWeatherAndForecastSubscriber());

        this.geocodingUsingZipCode.setZipCode(zipCode);
        this.geocodingUsingZipCode.execute(new GeocodingUsingZipCodeSubscriber());
    }

    private void renderWeather(CurrentWeatherAndForecast currentWeatherAndForecast) {
        this.weatherDetailView.renderWeathers(currentWeatherAndForecast);
        this.loadingView.hideLoading();
    }

    private void renderLocationInfo(GeocodingResult geocodingResult) {
        this.weatherDetailView.renderLocationInfo(geocodingResult);
    }

    private class CurrentWeatherAndForecastSubscriber extends DefaultSubscriber<CurrentWeatherAndForecast> {
        @Override
        public void onError(Throwable e) {
            super.onError(e);
            Timber.e(e);
        }

        @Override
        public void onNext(CurrentWeatherAndForecast currentWeatherAndForecast) {
            WeatherDetailPresenter.this.renderWeather(currentWeatherAndForecast);
        }
    }

    private class GeocodingUsingZipCodeSubscriber extends DefaultSubscriber<GeocodingResult> {
        @Override
        public void onError(Throwable e) {
            super.onError(e);
            Timber.e(e);
        }

        @Override
        public void onNext(GeocodingResult geocodingResult) {
            WeatherDetailPresenter.this.renderLocationInfo(geocodingResult);
        }
    }
}
