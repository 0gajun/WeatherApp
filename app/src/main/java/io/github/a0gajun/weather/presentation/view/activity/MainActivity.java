/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.presentation.view.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import io.github.a0gajun.weather.R;
import io.github.a0gajun.weather.databinding.ActivityHomeBinding;
import io.github.a0gajun.weather.databinding.ActivityLayoutBinding;
import io.github.a0gajun.weather.presentation.di.HasComponent;
import io.github.a0gajun.weather.presentation.di.component.DaggerWeatherComponent;
import io.github.a0gajun.weather.presentation.di.component.WeatherComponent;
import io.github.a0gajun.weather.presentation.di.module.ActivityModule;
import io.github.a0gajun.weather.presentation.di.module.WeatherModule;
import io.github.a0gajun.weather.presentation.view.fragment.MainFragment;

public class MainActivity extends BaseActivity
        implements HasComponent<WeatherComponent> {

    private ActivityHomeBinding binding;

    private WeatherComponent weatherComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = DataBindingUtil.setContentView(this, R.layout.activity_home);

        initializeInjector();
        if (savedInstanceState == null) {
            addFragment(binding.fragmentContainer.getId(), new MainFragment());
        }
    }

    private void initializeInjector() {
        this.weatherComponent = DaggerWeatherComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(new ActivityModule(this))
                .weatherModule(new WeatherModule("223-0061")) // TODO: Handling correctly
                .build();
    }

    @Override
    public WeatherComponent getComponent() {
        return this.weatherComponent;
    }
}
