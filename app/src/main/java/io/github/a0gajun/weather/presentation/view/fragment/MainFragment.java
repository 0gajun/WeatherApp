/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.presentation.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.Collection;

import javax.inject.Inject;

import io.github.a0gajun.weather.R;
import io.github.a0gajun.weather.databinding.FragmentMainBinding;
import io.github.a0gajun.weather.domain.model.CurrentWeather;
import io.github.a0gajun.weather.domain.model.FiveDayForecast;
import io.github.a0gajun.weather.presentation.di.component.WeatherComponent;
import io.github.a0gajun.weather.presentation.view.MainView;
import io.github.a0gajun.weather.presentation.view.adapter.EveryThreeHoursForecastAdapter;
import io.github.a0gajun.weather.presentation.view.presenter.MainPresenter;

/**
 * Created by Junya Ogasawara on 1/9/17.
 */

public class MainFragment extends BaseFragment implements MainView {

    @Inject MainPresenter mainPresenter;
    @Inject EveryThreeHoursForecastAdapter everyThreeHoursForecastAdapter;

    private FragmentMainBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getComponent(WeatherComponent.class).inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.binding = FragmentMainBinding.bind(getView());
        setUpRecyclerView();

        this.mainPresenter.setView(this);
        if (savedInstanceState == null) {
            this.mainPresenter.initialize();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        this.mainPresenter.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.mainPresenter.pause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mainPresenter.destroy();
    }

    @Override
    public void renderCurrentWeather(CurrentWeather currentWeather) {
        // TODO: cannot use databinding due to lombok...
        binding.setCurrentWeather(currentWeather);
        binding.cityName.setText(currentWeather.getCityName());
        binding.countryName.setText(currentWeather.getCountryName());
        binding.temperature.setText(getString(R.string.celsius_degree_fmt, currentWeather.getTemperature()));
        binding.weather.setText(currentWeather.getWeather());
        binding.weatherDesc.setText(currentWeather.getWeatherDescription());
        binding.weatherIcon.setImageResource(currentWeather.getWeatherIconResId());
    }

    @Override
    public void renderEveryThreeHoursForecast(Collection<FiveDayForecast.EveryThreeHoursForecastData> forecastData) {
        this.everyThreeHoursForecastAdapter.setForecastDataList(forecastData);
    }

    private Context context() {
        return this.getActivity().getApplicationContext();
    }

    private void setUpRecyclerView() {
        this.binding.rvForecast.setLayoutManager(new LinearLayoutManager(context(), LinearLayoutManager.HORIZONTAL, false));
        this.binding.rvForecast.setAdapter(this.everyThreeHoursForecastAdapter);
    }
}
