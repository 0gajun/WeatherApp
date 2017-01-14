/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.presentation.view.adapter;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.github.a0gajun.weather.R;
import io.github.a0gajun.weather.databinding.LayoutWeatherAndForecastCardBinding;
import io.github.a0gajun.weather.domain.model.CurrentWeather;
import io.github.a0gajun.weather.domain.model.CurrentWeatherAndForecast;
import io.github.a0gajun.weather.domain.model.FiveDayForecast;

/**
 * Created by Junya Ogasawara on 1/14/17.
 */

public class HomeWeathersAdapter extends RecyclerView.Adapter<HomeWeathersAdapter.BindingHolder> {

    private static final int FORECAST_MAX_ENTRY = 8;

    private final LayoutInflater layoutInflater;

    private List<CurrentWeatherAndForecast> currentWeatherAndForecasts;

    @Inject
    public HomeWeathersAdapter(Activity context) {
        this.layoutInflater = (LayoutInflater) context.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
        this.currentWeatherAndForecasts = Collections.EMPTY_LIST;
    }

    public void setCurrentWeatherAndForecasts(Collection<CurrentWeatherAndForecast> currentWeatherAndForecasts) {
        this.currentWeatherAndForecasts = (List<CurrentWeatherAndForecast>) currentWeatherAndForecasts;
        notifyDataSetChanged();
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = this.layoutInflater.inflate(R.layout.layout_weather_and_forecast_card, parent, false);
        return new BindingHolder(view);
    }

    @Override
    public void onBindViewHolder(BindingHolder holder, int position) {
        final CurrentWeatherAndForecast data = this.currentWeatherAndForecasts.get(position);
        final CurrentWeather weather = data.getCurrentWeather();
        final FiveDayForecast forecast = data.getFiveDayForecast();

        final LayoutWeatherAndForecastCardBinding binding = holder.getBinding();
        final Context context = holder.itemView.getContext();
        binding.locationTxt.setText(weather.getCityName() + "," + weather.getCountryName());
        binding.temperature.setText(context.getString(R.string.celsius_degree_fmt, weather.getTemperature()));
        binding.weather.setText(weather.getWeather());
        binding.weatherDesc.setText(weather.getWeatherDescription());
        binding.weatherIcon.setImageResource(weather.getWeatherIconResId());

        setUpRecyclerView(binding, context, forecast);
    }

    private void setUpRecyclerView(LayoutWeatherAndForecastCardBinding binding, Context context, FiveDayForecast forecast) {
        binding.rvForecast.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        EveryThreeHoursForecastAdapter adapter = new EveryThreeHoursForecastAdapter(context);
        binding.rvForecast.setAdapter(adapter);
        adapter.setForecastDataList(extractForecastDataList(forecast));
        adapter.notifyDataSetChanged();
    }

    private List<FiveDayForecast.EveryThreeHoursForecastData> extractForecastDataList(FiveDayForecast forecast) {
        if (forecast.getForecastData().size() < FORECAST_MAX_ENTRY) {
            return new ArrayList<>(forecast.getForecastData());
        }
        return new ArrayList<>(forecast.getForecastData().subList(0, FORECAST_MAX_ENTRY));
    }

    @Override
    public int getItemCount() {
        return currentWeatherAndForecasts.size();
    }

    public static class BindingHolder extends RecyclerView.ViewHolder {
        private LayoutWeatherAndForecastCardBinding binding;

        BindingHolder(View itemView) {
            super(itemView);
            this.binding = DataBindingUtil.bind(itemView);
        }

        public LayoutWeatherAndForecastCardBinding getBinding() {
            return binding;
        }
    }
}
