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
import io.github.a0gajun.weather.presentation.di.component.ActivityComponent;
import io.github.a0gajun.weather.presentation.di.component.DaggerActivityComponent;
import io.github.a0gajun.weather.presentation.di.module.ActivityModule;
import io.github.a0gajun.weather.presentation.view.fragment.WatchingLocationRegistrationFragment;

/**
 * Created by Junya Ogasawara on 1/15/17.
 */

public class WatchingLocationRegistrationActivity extends BaseActivity
        implements HasComponent<ActivityComponent> {

    private ActivityLayoutBinding binding;
    private ActivityComponent activityComponent;

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, WatchingLocationRegistrationActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.binding = DataBindingUtil.setContentView(this, R.layout.activity_layout);

        initializeInjector();

        if (savedInstanceState == null) {
            addFragment(R.id.fragment_container, new WatchingLocationRegistrationFragment());
        }
    }

    private void initializeInjector() {
        this.activityComponent = DaggerActivityComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(new ActivityModule(this))
                .build();
    }

    @Override
    public ActivityComponent getComponent() {
        return this.activityComponent;
    }
}
