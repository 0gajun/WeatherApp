/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.presentation.view.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import io.github.a0gajun.weather.R;
import io.github.a0gajun.weather.databinding.ActivityHomeBinding;
import io.github.a0gajun.weather.presentation.di.HasComponent;
import io.github.a0gajun.weather.presentation.di.component.DaggerWeatherComponent;
import io.github.a0gajun.weather.presentation.di.component.WeatherComponent;
import io.github.a0gajun.weather.presentation.di.module.ActivityModule;
import io.github.a0gajun.weather.presentation.di.module.WeatherModule;
import io.github.a0gajun.weather.presentation.view.fragment.HomeFragment;

/**
 * Created by Junya Ogasawara on 1/13/17.
 */

public class HomeActivity extends BaseActivity implements HasComponent<WeatherComponent> {

    private WeatherComponent weatherComponent;
    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = DataBindingUtil.setContentView(this, R.layout.activity_home);

        initializeInjector();

        if (savedInstanceState == null) {
            addFragment(this.binding.fragmentContainer.getId(), new HomeFragment());
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
