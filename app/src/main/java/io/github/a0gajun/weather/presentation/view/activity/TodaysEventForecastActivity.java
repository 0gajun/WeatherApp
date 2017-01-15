/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.presentation.view.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import io.github.a0gajun.weather.R;
import io.github.a0gajun.weather.databinding.ActivityLayoutBinding;
import io.github.a0gajun.weather.presentation.di.HasComponent;
import io.github.a0gajun.weather.presentation.di.component.DaggerWeatherComponent;
import io.github.a0gajun.weather.presentation.di.component.WeatherComponent;
import io.github.a0gajun.weather.presentation.di.module.ActivityModule;
import io.github.a0gajun.weather.presentation.di.module.WeatherModule;
import io.github.a0gajun.weather.presentation.view.fragment.TodaysEventForecastFragment;

/**
 * Created by Junya Ogasawara on 1/16/17.
 */

public class TodaysEventForecastActivity extends BaseActivity implements HasComponent<WeatherComponent> {

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, TodaysEventForecastActivity.class);
    }

    private WeatherComponent weatherComponent;
    private ActivityLayoutBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = DataBindingUtil.setContentView(this, R.layout.activity_layout);

        initializeInjector();

        if (savedInstanceState == null) {
            addFragment(R.id.fragment_container, new TodaysEventForecastFragment());
        }
    }

    private void initializeInjector() {
        this.weatherComponent = DaggerWeatherComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(new ActivityModule(this))
                .weatherModule(new WeatherModule())
                .build();
    }

    @Override
    public WeatherComponent getComponent() {
        return this.weatherComponent;
    }
}
