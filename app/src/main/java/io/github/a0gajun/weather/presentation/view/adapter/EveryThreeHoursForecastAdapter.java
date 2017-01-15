/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.presentation.view.adapter;

import android.app.Service;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.threeten.bp.format.DateTimeFormatter;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.github.a0gajun.weather.R;
import io.github.a0gajun.weather.databinding.LayoutEveryThreeHoursForecastColBinding;
import io.github.a0gajun.weather.domain.model.FiveDayForecast;


/**
 * Created by Junya Ogasawara on 1/10/17.
 */

public class EveryThreeHoursForecastAdapter extends RecyclerView.Adapter<EveryThreeHoursForecastAdapter.BindingHolder> {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    private final LayoutInflater layoutInflater;
    private List<FiveDayForecast.EveryThreeHoursForecastData> forecastDataList;

    @Inject
    public EveryThreeHoursForecastAdapter(Context context) {
        this.layoutInflater = (LayoutInflater) context.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
        this.forecastDataList = Collections.emptyList();
    }

    public void setForecastDataList(Collection<FiveDayForecast.EveryThreeHoursForecastData> forecastDataList) {
        this.forecastDataList = (List<FiveDayForecast.EveryThreeHoursForecastData>) forecastDataList;
        this.notifyDataSetChanged();
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = this.layoutInflater.inflate(R.layout.layout_every_three_hours_forecast_col, parent, false);
        return new BindingHolder(view);
    }

    @Override
    public void onBindViewHolder(BindingHolder holder, int position) {
        final FiveDayForecast.EveryThreeHoursForecastData forecast = this.forecastDataList.get(position);

        final LayoutEveryThreeHoursForecastColBinding binding = holder.getBinding();
        binding.temperature.setText(
                binding.temperature.getContext().getString(R.string.celsius_degree_fmt, forecast.getTemperature()));
        binding.weather.setText(forecast.getWeather());
        binding.weatherIcon.setImageResource(forecast.getWeatherIconResId());
        binding.time.setText(forecast.getForecastAt().format(TIME_FORMATTER));
    }

    @Override
    public int getItemCount() {
        return (this.forecastDataList != null) ? this.forecastDataList.size() : 0;
    }

    static final class BindingHolder extends RecyclerView.ViewHolder {
        private final LayoutEveryThreeHoursForecastColBinding binding;

        public BindingHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        public LayoutEveryThreeHoursForecastColBinding getBinding() {
            return binding;
        }
    }
}
