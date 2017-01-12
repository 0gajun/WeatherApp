/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.presentation.view.presenter;

import android.location.Location;

import com.annimon.stream.Stream;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Named;

import io.github.a0gajun.weather.data.entity.owm_common.Main;
import io.github.a0gajun.weather.domain.executor.PostExecutionThread;
import io.github.a0gajun.weather.domain.model.CurrentWeather;
import io.github.a0gajun.weather.domain.model.FiveDayForecast;
import io.github.a0gajun.weather.domain.usecase.DefaultSubscriber;
import io.github.a0gajun.weather.domain.usecase.GetCurrentLocation;
import io.github.a0gajun.weather.domain.usecase.UseCase;
import io.github.a0gajun.weather.presentation.view.MainView;
import rx.Subscriber;
import timber.log.Timber;

/**
 * Created by Junya Ogasawara on 1/9/17.
 */

public class MainPresenter implements Presenter {

    private final UseCase currentWeatherUseCase;
    private final UseCase fiveDayForecastUseCase;
    private final UseCase currentLocationUseCase;

    private MainView mainView;
    private CurrentWeather currentWeather;
    private FiveDayForecast fiveDayForecast;

    @Inject
    public MainPresenter(@Named("currentWeather") UseCase currentWeatherUseCase,
                         @Named("fiveDayForecast") UseCase fiveDayForecastUseCase,
                         GetCurrentLocation getCurrentLocation) {
        this.currentWeatherUseCase = currentWeatherUseCase;
        this.fiveDayForecastUseCase = fiveDayForecastUseCase;
        this.currentLocationUseCase = getCurrentLocation;
    }

    public void setView(MainView mainView) {
        this.mainView = mainView;
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        this.mainView = null;
        this.currentWeather = null;
        this.currentWeatherUseCase.unsubscribe();
        this.fiveDayForecastUseCase.unsubscribe();
        this.currentLocationUseCase.unsubscribe();
    }

    public void initialize() {
        loadCurrentWeather();
        loadEveryThreeHoursForecast();
        this.currentLocationUseCase.execute(new Subscriber<Location>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Location location) {
                Timber.d(location.toString());
            }
        });
    }

    private void setCurrentWeather(CurrentWeather currentWeather) {
        this.currentWeather = currentWeather;
    }

    public void setFiveDayForecast(FiveDayForecast fiveDayForecast) {
        this.fiveDayForecast = fiveDayForecast;
    }

    private void render() {
        if (this.currentWeather != null) {
            this.mainView.renderCurrentWeather(this.currentWeather);
        }
        if (this.fiveDayForecast != null) {
            //this.mainView.renderEveryThreeHoursForecast(new ArrayList<>(this.fiveDayForecast.getForecastData().subList(0, 6)));
            this.mainView.renderEveryThreeHoursForecast(this.fiveDayForecast.getForecastData());
        }
    }

    private void loadCurrentWeather() {
        this.currentWeatherUseCase.execute(new CurrentWeatherSubscriber());
    }

    private void loadEveryThreeHoursForecast() {
        this.fiveDayForecastUseCase.execute(new FiveDayForecastSubscriber());
    }

    private final class CurrentWeatherSubscriber extends DefaultSubscriber<CurrentWeather> {
        @Override
        public void onError(Throwable e) {
            Timber.e(e);
            super.onError(e);
        }

        @Override
        public void onNext(CurrentWeather currentWeather) {
            MainPresenter.this.setCurrentWeather(currentWeather);
            MainPresenter.this.render();
        }
    }

    private final class FiveDayForecastSubscriber extends DefaultSubscriber<FiveDayForecast> {
        @Override
        public void onError(Throwable e) {
            Timber.e(e);
            super.onError(e);
        }

        @Override
        public void onNext(FiveDayForecast fiveDayForecast) {
            MainPresenter.this.setFiveDayForecast(fiveDayForecast);
            MainPresenter.this.render();
        }
    }


}
