/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.presentation.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import javax.inject.Inject;

import io.github.a0gajun.weather.R;
import io.github.a0gajun.weather.databinding.FragmentTodaysEventForecastBinding;
import io.github.a0gajun.weather.domain.model.TodaysEventAndWeather;
import io.github.a0gajun.weather.presentation.di.component.WeatherComponent;
import io.github.a0gajun.weather.presentation.view.RequestPermissionView;
import io.github.a0gajun.weather.presentation.view.TodaysEventForecastView;
import io.github.a0gajun.weather.presentation.view.activity.BaseActivity;
import io.github.a0gajun.weather.presentation.view.adapter.TodaysEventForecastAdapter;
import io.github.a0gajun.weather.presentation.view.presenter.PermissionPresenter;
import io.github.a0gajun.weather.presentation.view.presenter.TodaysEventForecastPresenter;

/**
 * Created by Junya Ogasawara on 1/16/17.
 */

public class TodaysEventForecastFragment extends BaseFragment
        implements TodaysEventForecastView, RequestPermissionView {

    private FragmentTodaysEventForecastBinding binding;

    @Inject TodaysEventForecastPresenter todaysEventForecastPresenter;
    @Inject PermissionPresenter permissionPresenter;
    @Inject TodaysEventForecastAdapter todaysEventForecastAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_todays_event_forecast, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.getComponent(WeatherComponent.class).inject(this);

        this.binding = FragmentTodaysEventForecastBinding.bind(getView());

        setUpToolbar();
        setUpRecyclerView();

        this.todaysEventForecastPresenter.setView(this);
        this.permissionPresenter.setView(this);
        if (savedInstanceState == null) {
            this.todaysEventForecastPresenter.loadTodaysEventWeather();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        this.permissionPresenter.resume();
        this.todaysEventForecastPresenter.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.permissionPresenter.pause();
        this.todaysEventForecastPresenter.pause();
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.todaysEventForecastPresenter.destroy();
        this.permissionPresenter.destroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.binding.rvEventForecast.setAdapter(null);
    }

    @Override
    public void requireCalendarPermission() {
        this.permissionPresenter.requestCalendarPermission(getActivity());
    }

    @Override
    public void render(List<TodaysEventAndWeather> todaysEventAndWeathers) {
        this.todaysEventForecastAdapter.setForecastDataList(todaysEventAndWeathers);
    }

    @Override
    public void requestPermission(String permission, PermissionPresenter.PermissionCode permissionCode) {
        // no-op
    }

    @Override
    public void permissionAccepted(PermissionPresenter.PermissionCode permissionCode) {
        switch (permissionCode) {
            case CALENDAR:
                this.todaysEventForecastPresenter.loadTodaysEventWeatherWithProperPermission();
        }
    }

    @Override
    public void permissionDenied(PermissionPresenter.PermissionCode permissionCode) {

    }

    @Override
    public void showRationale(PermissionPresenter.PermissionCode permissionCode) {

    }

    @Subscribe
    public void onRequestPermissionsResultEvent(final BaseActivity.OnRequestPermissionResultEvent event) {
        if (PermissionPresenter.PermissionCode.isHandledRequestCode(event.requestCode)) {
            this.permissionPresenter.checkGrantedPermissions(event.grantResults, event.requestCode);
        }
    }

    private void setUpRecyclerView() {
        this.binding.rvEventForecast.setLayoutManager(new LinearLayoutManager(getContext()));
        this.binding.rvEventForecast.setAdapter(todaysEventForecastAdapter);
    }

    private void setUpToolbar() {
        this.binding.toolbarBinding.toolbar.setTitle("Today's schedule and Forecast");
        this.binding.toolbarBinding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        this.binding.toolbarBinding.toolbar.setNavigationOnClickListener(v -> EventBus.getDefault().post(new BaseActivity.FinishActivityEvent()));
    }
}
