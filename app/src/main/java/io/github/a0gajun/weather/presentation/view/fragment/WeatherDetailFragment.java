/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.presentation.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.github.a0gajun.weather.R;
import io.github.a0gajun.weather.databinding.FragmentWeatherDetailBinding;

/**
 * Created by Junya Ogasawara on 1/15/17.
 */

public class WeatherDetailFragment extends BaseFragment {

    private FragmentWeatherDetailBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_weather_detail, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.binding = FragmentWeatherDetailBinding.bind(getView());
    }
}
