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
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Collection;

import javax.inject.Inject;

import io.github.a0gajun.weather.R;
import io.github.a0gajun.weather.databinding.FragmentHomeBinding;
import io.github.a0gajun.weather.domain.model.CurrentWeatherAndForecast;
import io.github.a0gajun.weather.presentation.di.component.WeatherComponent;
import io.github.a0gajun.weather.presentation.view.HomeView;
import io.github.a0gajun.weather.presentation.view.LoadingView;
import io.github.a0gajun.weather.presentation.view.RequestPermissionView;
import io.github.a0gajun.weather.presentation.view.activity.BaseActivity;
import io.github.a0gajun.weather.presentation.view.adapter.HomeWeathersAdapter;
import io.github.a0gajun.weather.presentation.view.presenter.HomePresenter;
import io.github.a0gajun.weather.presentation.view.presenter.PermissionPresenter;

/**
 * Created by Junya Ogasawara on 1/14/17.
 */

public class HomeFragment extends BaseFragment implements HomeView,
        LoadingView, RequestPermissionView, SwipeRefreshLayout.OnRefreshListener {

    @Inject HomePresenter homePresenter;
    @Inject PermissionPresenter permissionPresenter;
    @Inject HomeWeathersAdapter homeWeathersAdapter;
    private FragmentHomeBinding binding;

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.getComponent(WeatherComponent.class).inject(this);
        this.binding = FragmentHomeBinding.bind(getView());

        setUpRecyclerView();
        setUpSwipeRefreshLayout();

        this.homePresenter.setView(this);
        this.permissionPresenter.setView(this);

        if (savedInstanceState == null) {
            this.homePresenter.loadCurrentLocationWeather();
            this.homePresenter.loadRegisteredWeathers();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.binding.rvWeathers.setAdapter(null);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void renderWeathers(Collection<CurrentWeatherAndForecast> currentWeatherAndForecast) {
        this.homeWeathersAdapter.setCurrentWeatherAndForecasts(currentWeatherAndForecast);
        hideLoading();
    }

    @Override
    public void requireLocationPermission() {
        this.permissionPresenter.requestLocationPermission(getActivity());
    }

    @Override
    public void showLoading() {
        this.binding.swipeRefresh.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        this.binding.swipeRefresh.setRefreshing(false);
    }

    @Override
    public void showError(String errorMsg) {

    }

    @Override
    public Context context() {
        return null;
    }

    @Override
    public void requestPermission(String permission, PermissionPresenter.PermissionCode permissionCode) {
        // no-op
    }

    @Override
    public void permissionAccepted(PermissionPresenter.PermissionCode permissionCode) {
        switch (permissionCode) {
            case LOCATION:
                this.homePresenter.loadCurrentLocationWeatherWithProperPermission();
                break;
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
                    this.permissionPresenter.requestLocationPermissionWithoutRationale(getActivity());
                })
                .show();
    }

    @Override
    public void onRefresh() {
        this.homePresenter.reload();
    }

    @Subscribe
    public void onRequestPermissionsResultEvent(final BaseActivity.OnRequestPermissionResultEvent event) {
        if (PermissionPresenter.PermissionCode.isHandledRequestCode(event.requestCode)) {
            this.permissionPresenter.checkGrantedPermissions(event.grantResults, event.requestCode);
        }
    }

    private void setUpRecyclerView() {
        this.binding.rvWeathers.setLayoutManager(new LinearLayoutManager(getContext()));
        this.binding.rvWeathers.setAdapter(this.homeWeathersAdapter);
    }

    private void setUpSwipeRefreshLayout() {
        this.binding.swipeRefresh.setOnRefreshListener(this);
    }
}
