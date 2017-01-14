/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.presentation.view.component;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;

import io.github.a0gajun.weather.R;
import io.github.a0gajun.weather.databinding.LayoutWeatherAndForecastCardBinding;
import io.github.a0gajun.weather.domain.model.CurrentWeatherAndForecast;

/**
 * Created by Junya Ogasawara on 1/14/17.
 */

public class WeatherCardView extends CardView {

    private LayoutWeatherAndForecastCardBinding binding;

    public WeatherCardView(Context context) {
        this(context, null, 0);
    }

    public WeatherCardView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WeatherCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initialize(context);
    }

    private void initialize(Context context) {
        View view = View.inflate(context, R.layout.layout_weather_and_forecast_card, this);
        this.binding = DataBindingUtil.bind(view);
    }

    public void setData(CurrentWeatherAndForecast data) {
        this.binding.locationTxt.setText(data.getCurrentWeather().getCityName());
    }
}
