/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.presentation.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import timber.log.Timber;

/**
 * Created by Junya Ogasawara on 1/14/17.
 */

public class HomeFragment extends BaseFragment implements HomeView,
        LoadingView, RequestPermissionView {

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

        this.homePresenter.setView(this, this);
        this.permissionPresenter.setView(this);

        if (savedInstanceState == null) {
            this.homePresenter.loadWeathers();
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
        Timber.d("renderWeathers");
        this.homeWeathersAdapter.setCurrentWeatherAndForecasts(currentWeatherAndForecast);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

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

    }

    @Override
    public void permissionAccepted(PermissionPresenter.PermissionCode permissionCode) {

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
        this.binding.rvWeathers.setLayoutManager(new LinearLayoutManager(getContext()));
        this.binding.rvWeathers.setAdapter(this.homeWeathersAdapter);
    }
}
