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

import com.annimon.stream.Stream;

import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.github.a0gajun.weather.R;
import io.github.a0gajun.weather.databinding.LayoutEventWeatherRowBinding;
import io.github.a0gajun.weather.domain.model.CalendarEvent;
import io.github.a0gajun.weather.domain.model.FiveDayForecast;
import io.github.a0gajun.weather.domain.model.TodaysEventAndWeather;
import timber.log.Timber;


/**
 * Created by Junya Ogasawara on 1/10/17.
 */

public class TodaysEventForecastAdapter extends RecyclerView.Adapter<TodaysEventForecastAdapter.BindingHolder> {

    private final LayoutInflater layoutInflater;
    private final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private List<TodaysEventAndWeather> todaysEventAndWeathers;

    @Inject
    public TodaysEventForecastAdapter(Context context) {
        this.layoutInflater = (LayoutInflater) context.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
        this.todaysEventAndWeathers = Collections.emptyList();
    }

    public void setForecastDataList(List<TodaysEventAndWeather> todaysEventAndWeathers) {
        this.todaysEventAndWeathers = new ArrayList<>(todaysEventAndWeathers);
        Collections.sort(this.todaysEventAndWeathers,
                (e1, e2) -> e1.getCalendarEvent().getStartTime().compareTo(e2.getCalendarEvent().getStartTime()));
        this.notifyDataSetChanged();
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = this.layoutInflater.inflate(R.layout.layout_event_weather_row, parent, false);
        return new BindingHolder(view);
    }

    @Override
    public void onBindViewHolder(BindingHolder holder, int position) {
        final TodaysEventAndWeather data = this.todaysEventAndWeathers.get(position);

        final LayoutEventWeatherRowBinding binding = holder.getBinding();
        final FiveDayForecast.EveryThreeHoursForecastData forecast = data.getForecastData();
        final CalendarEvent event = data.getCalendarEvent();

        binding.eventTime.setText(event.getStartTime().format(TIME_FORMATTER)
                + "-" + event.getEndTime().format(TIME_FORMATTER));
        binding.eventTitle.setText(event.getTitle());
        binding.eventLocation.setText(event.getLocation());

        if (forecast != null) {
            binding.labelNoForecastData.setVisibility(View.GONE);
            binding.temperature.setVisibility(View.VISIBLE);
            binding.weather.setVisibility(View.VISIBLE);
            binding.time.setVisibility(View.VISIBLE);
            binding.weatherIcon.setVisibility(View.VISIBLE);
            binding.temperature.setText(
                    binding.temperature.getContext().getString(R.string.celsius_degree_fmt, forecast.getTemperature()));
            binding.weather.setText(forecast.getWeather());
            binding.weatherIcon.setImageResource(forecast.getWeatherIconResId());
            binding.time.setText(forecast.getForecastAt().format(TIME_FORMATTER));
        } else {
            binding.labelNoForecastData.setVisibility(View.VISIBLE);
            binding.temperature.setVisibility(View.GONE);
            binding.weather.setVisibility(View.GONE);
            binding.time.setVisibility(View.GONE);
            binding.weatherIcon.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return (this.todaysEventAndWeathers != null) ? this.todaysEventAndWeathers.size() : 0;
    }

    static final class BindingHolder extends RecyclerView.ViewHolder {
        private final LayoutEventWeatherRowBinding binding;

        public BindingHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        public LayoutEventWeatherRowBinding getBinding() {
            return binding;
        }
    }
}
