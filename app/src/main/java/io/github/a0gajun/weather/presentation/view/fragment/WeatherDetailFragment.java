/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.presentation.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import io.github.a0gajun.weather.R;
import io.github.a0gajun.weather.databinding.FragmentWeatherDetailBinding;
import io.github.a0gajun.weather.domain.model.CurrentWeather;
import io.github.a0gajun.weather.domain.model.CurrentWeatherAndForecast;
import io.github.a0gajun.weather.domain.model.GeocodingResult;
import io.github.a0gajun.weather.presentation.di.component.WeatherComponent;
import io.github.a0gajun.weather.presentation.view.LoadingView;
import io.github.a0gajun.weather.presentation.view.WeatherDetailView;
import io.github.a0gajun.weather.presentation.view.activity.BaseActivity;
import io.github.a0gajun.weather.presentation.view.adapter.WeatherDetailForecastAdapter;
import io.github.a0gajun.weather.presentation.view.presenter.WeatherDetailPresenter;

/**
 * Created by Junya Ogasawara on 1/15/17.
 */

public class WeatherDetailFragment extends BaseFragment implements WeatherDetailView, LoadingView {

    @Inject WeatherDetailPresenter weatherDetailPresenter;
    @Inject WeatherDetailForecastAdapter weatherDetailForecastAdapter;
    private FragmentWeatherDetailBinding binding;
    private String zipCode;

    public static WeatherDetailFragment newInstance(final String zipCode) {
        WeatherDetailFragment fragment = new WeatherDetailFragment();

        Bundle bundle = new Bundle();
        bundle.putString(INTENT_KEY.ZIP_CODE.name(), zipCode);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_weather_detail, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.getComponent(WeatherComponent.class).inject(this);

        this.zipCode = getArguments().getString(INTENT_KEY.ZIP_CODE.name());
        this.binding = FragmentWeatherDetailBinding.bind(getView());

        setUpToolbar();
        setUpRecyclerView();

        this.weatherDetailPresenter.setView(this);
        if (savedInstanceState == null) {
            this.binding.toolbarBinding.toolbar.setTitle(zipCode); // Initial value
            this.weatherDetailPresenter.loadData(zipCode);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        this.weatherDetailPresenter.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        this.weatherDetailPresenter.resume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.weatherDetailPresenter.destroy();
    }

    @Override
    public void renderLocationInfo(GeocodingResult geocodingResult) {
        this.binding.toolbarBinding.toolbar.setTitle(geocodingResult.getCityName() + ", " + geocodingResult.getCountryName());
    }

    @Override
    public void renderWeathers(CurrentWeatherAndForecast data) {
        final CurrentWeather current = data.getCurrentWeather();
        this.binding.temperature.setText(getString(R.string.celsius_degree_fmt, current.getTemperature()));
        this.binding.currentWeatherIcon.setImageResource(current.getWeatherIconResId());
        this.binding.weather.setText(current.getWeather());
        this.binding.weatherDesc.setText(current.getWeatherDescription());
        this.binding.wind.setText(getString(R.string.meter_per_sec_fmt, current.getWindSpeed()));
        this.binding.cloudiness.setText(getString(R.string.int_percent_fmt, current.getCloudiness()));
        this.binding.humidity.setText(getString(R.string.int_percent_fmt, current.getHumidity()));
        this.binding.observersLocation.setText(current.getCityName() + ", " + current.getCountryName());

        this.weatherDetailForecastAdapter.setForecastData(data.getFiveDayForecast());
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showError(String errorMsg) {

    }

    @Override
    public Context context() {
        return null;
    }

    private void setUpToolbar() {
        this.binding.toolbarBinding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        this.binding.toolbarBinding.toolbar.setNavigationOnClickListener(v ->
                EventBus.getDefault().post(new BaseActivity.FinishActivityEvent()));
    }

    private void setUpRecyclerView() {
        this.binding.rvForecast.setLayoutManager(new LinearLayoutManager(getContext()));
        this.binding.rvForecast.setAdapter(this.weatherDetailForecastAdapter);
    }

    enum INTENT_KEY {
        ZIP_CODE
    }
}
