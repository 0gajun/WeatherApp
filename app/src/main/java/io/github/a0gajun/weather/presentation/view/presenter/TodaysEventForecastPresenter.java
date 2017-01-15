/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.presentation.view.presenter;

import java.util.List;

import javax.inject.Inject;

import io.github.a0gajun.weather.domain.model.TodaysEventAndWeather;
import io.github.a0gajun.weather.domain.usecase.DefaultSubscriber;
import io.github.a0gajun.weather.domain.usecase.GetTodaysEventAndWeather;
import io.github.a0gajun.weather.presentation.view.RequestPermissionView;
import io.github.a0gajun.weather.presentation.view.TodaysEventForecastView;
import timber.log.Timber;

/**
 * Created by Junya Ogasawara on 1/16/17.
 */

public class TodaysEventForecastPresenter implements Presenter {

    private final GetTodaysEventAndWeather getTodaysEventAndWeather;

    private TodaysEventForecastView todaysEventForecastView;
    private RequestPermissionView requestPermissionView;

    @Inject
    public TodaysEventForecastPresenter(GetTodaysEventAndWeather getTodaysEventAndWeather) {
        this.getTodaysEventAndWeather = getTodaysEventAndWeather;
    }

    public <T extends TodaysEventForecastView & RequestPermissionView> void setView(T view) {
        this.todaysEventForecastView = view;
        this.requestPermissionView = view;
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        this.requestPermissionView = null;
        this.todaysEventForecastView = null;

        this.getTodaysEventAndWeather.unsubscribe();
    }

    public void loadTodaysEventWeather() {
        this.todaysEventForecastView.requireCalendarPermission();
    }

    public void loadTodaysEventWeatherWithProperPermission() {
        this.getTodaysEventAndWeather.execute(new LoadTodaysEventWeatherSubscriber());
    }

    private class LoadTodaysEventWeatherSubscriber extends DefaultSubscriber<List<TodaysEventAndWeather>> {
        @Override
        public void onNext(List<TodaysEventAndWeather> todaysEventAndWeather) {
            super.onNext(todaysEventAndWeather);

            TodaysEventForecastPresenter.this.todaysEventForecastView.render(todaysEventAndWeather);

            for (TodaysEventAndWeather e : todaysEventAndWeather) {
                Timber.d(e.getCalendarEvent().getTitle());
                Timber.d(e.getCalendarEvent().getLocation());

                if (e.getForecastData() != null) {
                    Timber.d(e.getForecastData().getWeather());
                }
            }


        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            Timber.e(e);
        }
    }
}
