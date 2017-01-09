/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.presentation.view.presenter;

import javax.inject.Inject;
import javax.inject.Named;

import io.github.a0gajun.weather.data.entity.owm_common.Main;
import io.github.a0gajun.weather.domain.model.CurrentWeather;
import io.github.a0gajun.weather.domain.usecase.DefaultSubscriber;
import io.github.a0gajun.weather.domain.usecase.UseCase;
import io.github.a0gajun.weather.presentation.view.MainView;
import timber.log.Timber;

/**
 * Created by Junya Ogasawara on 1/9/17.
 */

public class MainPresenter implements Presenter {

    private final UseCase currentWeatherUseCase;
    private final UseCase fiveDayForecastUseCase;

    private MainView mainView;
    private CurrentWeather currentWeather;

    @Inject
    public MainPresenter(@Named("currentWeather") UseCase currentWeatherUseCase,
                         @Named("fiveDayForecast") UseCase fiveDayForecastUseCase) {
        this.currentWeatherUseCase = currentWeatherUseCase;
        this.fiveDayForecastUseCase = fiveDayForecastUseCase;
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
    }

    public void initialize() {
        loadCurrentWeather();
    }

    private void setCurrentWeather(CurrentWeather currentWeather) {
        this.currentWeather = currentWeather;
    }

    private void render() {
        if (this.currentWeather != null) {
            this.mainView.renderCurrentWeather(this.currentWeather);
        }
    }

    private void loadCurrentWeather() {
        this.currentWeatherUseCase.execute(new CurrentWeatherSubscriber());
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


}
