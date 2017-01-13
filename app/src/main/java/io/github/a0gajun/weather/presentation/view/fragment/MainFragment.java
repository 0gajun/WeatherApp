/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.presentation.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Collection;

import javax.inject.Inject;

import io.github.a0gajun.weather.R;
import io.github.a0gajun.weather.databinding.FragmentMainBinding;
import io.github.a0gajun.weather.domain.model.CurrentWeather;
import io.github.a0gajun.weather.domain.model.FiveDayForecast;
import io.github.a0gajun.weather.presentation.di.component.WeatherComponent;
import io.github.a0gajun.weather.presentation.view.MainView;
import io.github.a0gajun.weather.presentation.view.RequestPermissionView;
import io.github.a0gajun.weather.presentation.view.activity.BaseActivity;
import io.github.a0gajun.weather.presentation.view.adapter.EveryThreeHoursForecastAdapter;
import io.github.a0gajun.weather.presentation.view.presenter.MainPresenter;
import io.github.a0gajun.weather.presentation.view.presenter.PermissionPresenter;

/**
 * Created by Junya Ogasawara on 1/9/17.
 */

public class MainFragment extends BaseFragment implements MainView, RequestPermissionView {

    @Inject MainPresenter mainPresenter;
    @Inject PermissionPresenter permissionPresenter;
    @Inject EveryThreeHoursForecastAdapter everyThreeHoursForecastAdapter;

    private FragmentMainBinding binding;

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.getComponent(WeatherComponent.class).inject(this);

        this.binding = FragmentMainBinding.bind(getView());
        setUpRecyclerView();

        this.mainPresenter.setView(this);
        this.permissionPresenter.setView(this);
        if (savedInstanceState == null) {
            this.mainPresenter.loadCurrentWeather();
            this.mainPresenter.loadEveryThreeHoursForecast();
            this.permissionPresenter.requestLocationPermission(getActivity());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void onResume() {
        super.onResume();
        this.mainPresenter.resume();
        this.permissionPresenter.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.mainPresenter.pause();
        this.permissionPresenter.pause();
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.binding.rvForecast.setAdapter(null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mainPresenter.destroy();
        this.permissionPresenter.destroy();
    }

    @Subscribe
    public void onRequestPermissionsResultEvent(final BaseActivity.OnRequestPermissionResultEvent event) {
        if (PermissionPresenter.PermissionCode.isHandledRequestCode(event.requestCode)) {
            this.permissionPresenter.checkGrantedPermissions(event.grantResults, event.requestCode);
        }
    }

    @Override
    public void renderCurrentWeather(CurrentWeather currentWeather) {
        // TODO: cannot use databinding due to lombok...
        binding.setCurrentWeather(currentWeather);
        binding.cityName.setText(currentWeather.getCityName());
        binding.countryName.setText(currentWeather.getCountryName());
        binding.temperature.setText(getString(R.string.celsius_degree_fmt, currentWeather.getTemperature()));
        binding.weather.setText(currentWeather.getWeather());
        binding.weatherDesc.setText(currentWeather.getWeatherDescription());
        binding.weatherIcon.setImageResource(currentWeather.getWeatherIconResId());
    }

    @Override
    public void renderEveryThreeHoursForecast(Collection<FiveDayForecast.EveryThreeHoursForecastData> forecastData) {
        this.everyThreeHoursForecastAdapter.setForecastDataList(forecastData);
    }

    @Override
    public void requestPermission(String permission, PermissionPresenter.PermissionCode permissionCode) {
        switch (permissionCode) {
            case LOCATION:
                this.permissionPresenter.requestLocationPermission(getActivity());
        }
    }

    @Override
    public void permissionAccepted(PermissionPresenter.PermissionCode permissionCode) {
        switch (permissionCode) {
            case LOCATION:
                this.mainPresenter.loadCurrentLocation();
        }
    }

    @Override
    public void permissionDenied(PermissionPresenter.PermissionCode permissionCode) {
        showSnackBar(getString(R.string.msg_permission_required_for_current_location_weather));
    }

    @Override
    public void showRationale(PermissionPresenter.PermissionCode permissionCode) {
        Snackbar.make(getView(), R.string.msg_permission_required_for_current_location_weather, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.label_permit, v -> {
                    this.permissionPresenter.requestLocationPermissionForcefully(getActivity());
                })
                .show();
    }

    private Context context() {
        return this.getActivity().getApplicationContext();
    }

    private void setUpRecyclerView() {
        this.binding.rvForecast.setLayoutManager(new LinearLayoutManager(context(), LinearLayoutManager.HORIZONTAL, false));
        this.binding.rvForecast.setAdapter(this.everyThreeHoursForecastAdapter);
    }
}
