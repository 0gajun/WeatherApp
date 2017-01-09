/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.presentation.view.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import io.github.a0gajun.weather.R;
import io.github.a0gajun.weather.databinding.ActivityLayoutBinding;
import io.github.a0gajun.weather.presentation.di.HasComponent;
import io.github.a0gajun.weather.presentation.di.component.DaggerWeatherComponent;
import io.github.a0gajun.weather.presentation.di.component.WeatherComponent;
import io.github.a0gajun.weather.presentation.di.module.WeatherModule;
import io.github.a0gajun.weather.presentation.view.fragment.MainFragment;

public class MainActivity extends BaseActivity
        implements HasComponent<WeatherComponent> {

    private ActivityLayoutBinding binding;

    private WeatherComponent weatherComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = DataBindingUtil.setContentView(this, R.layout.activity_layout);

        initializeInjector();
        if (savedInstanceState == null) {
            addFragment(binding.fragmentContainer.getId(), new MainFragment());
        }
    }

    private void initializeInjector() {
        this.weatherComponent = DaggerWeatherComponent.builder()
                .applicationComponent(getApplicationComponent())
                .weatherModule(new WeatherModule("111-1111")) // TODO: Handling correctly
                .build();
    }

    @Override
    public WeatherComponent getComponent() {
        return this.weatherComponent;
    }
}
