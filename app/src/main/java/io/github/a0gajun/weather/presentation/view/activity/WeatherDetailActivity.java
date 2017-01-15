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
import io.github.a0gajun.weather.presentation.view.fragment.WeatherDetailFragment;

/**
 * Created by Junya Ogasawara on 1/15/17.
 */

public class WeatherDetailActivity extends BaseActivity implements HasComponent<WeatherComponent> {

    enum INTENT_KEY {
        ZIP_CODE
    }

    public static Intent getCallingIntent(Context context, final String zipCode) {
        Intent intent = new Intent(context, WeatherDetailActivity.class);
        intent.putExtra(INTENT_KEY.ZIP_CODE.name(), zipCode);
        return intent;
    }

    private WeatherComponent weatherComponent;
    private ActivityLayoutBinding binding;
    private String zipCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = DataBindingUtil.setContentView(this, R.layout.activity_layout);

        this.zipCode = getIntent().getStringExtra(INTENT_KEY.ZIP_CODE.name());

        initializeInjector();

        if (savedInstanceState == null) {
            addFragment(R.id.fragment_container, WeatherDetailFragment.newInstance(zipCode));
        }
    }

    private void initializeInjector() {
        this.weatherComponent = DaggerWeatherComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(new ActivityModule(this))
                .weatherModule(new WeatherModule(this.zipCode))
                .build();
    }

    @Override
    public WeatherComponent getComponent() {
        return this.weatherComponent;
    }
}
