/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.presentation.view.adapter;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.github.a0gajun.weather.R;
import io.github.a0gajun.weather.databinding.LayoutWeatherDetailForecastRowBinding;
import io.github.a0gajun.weather.domain.model.FiveDayForecast;

/**
 * Created by Junya Ogasawara on 1/15/17.
 */

public class WeatherDetailForecastAdapter extends RecyclerView.Adapter<WeatherDetailForecastAdapter.BindingHolder> {

    private DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/dd");

    private final LayoutInflater layoutInflater;

    private List<FiveDayForecast.EveryThreeHoursForecastData> forecastDataList;

    @Inject
    public WeatherDetailForecastAdapter(Activity activity) {
        this.layoutInflater = (LayoutInflater) activity.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
        this.forecastDataList = Collections.emptyList();
    }

    public void setForecastData(FiveDayForecast fiveDayForecast) {
        this.forecastDataList = new ArrayList<>(fiveDayForecast.getForecastData());
        this.notifyDataSetChanged();
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = layoutInflater.inflate(R.layout.layout_weather_detail_forecast_row, parent, false);
        return new BindingHolder(view);
    }

    @Override
    public void onBindViewHolder(BindingHolder holder, int position) {
        final FiveDayForecast.EveryThreeHoursForecastData data = forecastDataList.get(position);
        final LayoutWeatherDetailForecastRowBinding binding = holder.getBinding();
        final Context context = holder.itemView.getContext();

        binding.temperature.setText(context.getString(R.string.celsius_degree_fmt, data.getTemperature()));
        binding.weather.setText(data.getWeather());
        binding.weatherIcon.setImageResource(data.getWeatherIconResId());

        final String timeStr = data.getForecastAt().format(TIME_FORMATTER);
        binding.time.setText(timeStr);

        if (TextUtils.equals(timeStr, "00:00")) { //TODO: Fix Logic
            binding.date.setVisibility(View.VISIBLE);
            binding.date.setText(data.getForecastAt().format(DATE_FORMATTER));
        } else {
            binding.date.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return forecastDataList.size();
    }

    static class BindingHolder extends RecyclerView.ViewHolder {
        private final LayoutWeatherDetailForecastRowBinding binding;

        BindingHolder(View itemView) {
            super(itemView);
            this.binding = LayoutWeatherDetailForecastRowBinding.bind(itemView);
        }

        LayoutWeatherDetailForecastRowBinding getBinding() {
            return this.binding;
        }
    }
}
