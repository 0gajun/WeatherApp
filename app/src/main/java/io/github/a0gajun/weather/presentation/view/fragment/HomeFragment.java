/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.presentation.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import io.github.a0gajun.weather.presentation.view.activity.TodaysEventForecastActivity;
import io.github.a0gajun.weather.presentation.view.activity.WatchingLocationRegistrationActivity;
import io.github.a0gajun.weather.presentation.view.activity.WeatherDetailActivity;
import io.github.a0gajun.weather.presentation.view.adapter.HomeWeathersAdapter;
import io.github.a0gajun.weather.presentation.view.presenter.HomePresenter;
import io.github.a0gajun.weather.presentation.view.presenter.PermissionPresenter;
import timber.log.Timber;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

/**
 * Created by Junya Ogasawara on 1/14/17.
 */

public class HomeFragment extends BaseFragment implements HomeView,
        LoadingView, RequestPermissionView, SwipeRefreshLayout.OnRefreshListener {

    private static final int WATCHING_LOCATION_REGISTRATION_REQUEST_CODE = 1;

    @Inject HomePresenter homePresenter;
    @Inject PermissionPresenter permissionPresenter;
    @Inject HomeWeathersAdapter homeWeathersAdapter;
    private ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new HomeWeathersAdapter.HomeWeathersItemTouchHelperCallback());
    private FragmentHomeBinding binding;

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
        setUpFAB();
        setUpToolbar();

        this.homePresenter.setView(this);
        this.permissionPresenter.setView(this);

        if (savedInstanceState == null) {
            this.homePresenter.loadCurrentLocationWeather();
            this.homePresenter.loadRegisteredWeathers();
        }

        showTutorialIfNeeded();
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
        this.homePresenter.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.permissionPresenter.pause();
        this.homePresenter.pause();
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.homePresenter.destroy();
        this.permissionPresenter.destroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.binding.rvWeathers.setAdapter(null);
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

    @Subscribe
    public void onRecyclerViewItemDeleteEvent(final HomeWeathersAdapter.RecyclerViewItemDeleteEvent event) {
        final CurrentWeatherAndForecast currentWeatherAndForecast
                = this.homeWeathersAdapter.deleteItem(event.deletedItemsAdapterPosition);
        final String zipCode = currentWeatherAndForecast.getZipCode();

        // TODO: Refactor, where should i put?
        Snackbar.make(getView(), "Unregistered", Snackbar.LENGTH_LONG)
                .setAction("UNDO", v -> this.homePresenter.reload())
                .addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        if (event == DISMISS_EVENT_ACTION) {
                            return;
                        }

                        Timber.d("Unregister from DB: " + zipCode);
                        HomeFragment.this.homePresenter.unregisterWatchingLocation(zipCode);
                    }
                })
                .show();
    }

    @Subscribe
    public void onViewDetailWeatherEvent(HomeWeathersAdapter.ViewDetailWeatherEvent event) {
        startActivity(WeatherDetailActivity.getCallingIntent(getContext(), event.zipCode));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case WATCHING_LOCATION_REGISTRATION_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    this.showLoading();
                    this.onRefresh();
                    return;
                }
            default:
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setUpRecyclerView() {
        this.binding.rvWeathers.setLayoutManager(new LinearLayoutManager(getContext()));
        this.itemTouchHelper.attachToRecyclerView(this.binding.rvWeathers);
        this.binding.rvWeathers.addItemDecoration(this.itemTouchHelper);
        this.binding.rvWeathers.setAdapter(this.homeWeathersAdapter);
    }

    private void setUpSwipeRefreshLayout() {
        this.binding.swipeRefresh.setOnRefreshListener(this);
    }

    private void setUpFAB() {
        this.binding.fab.setOnClickListener(v -> {
            startActivityForResult(WatchingLocationRegistrationActivity
                    .getCallingIntent(getContext()), WATCHING_LOCATION_REGISTRATION_REQUEST_CODE);
        });
    }

    private void setUpToolbar() {
        this.binding.toolbarBinding.toolbar.inflateMenu(R.menu.menu_calendar);
        MenuItem menuItem = binding.toolbarBinding.toolbar.getMenu().findItem(R.id.calendar_btn);
        menuItem.setOnMenuItemClickListener(v -> {
            startActivity(TodaysEventForecastActivity.getCallingIntent(getContext()));
            return true;
        });

    }

    private void showTutorialIfNeeded() {
        ShowcaseConfig showcaseConfig = new ShowcaseConfig();
        showcaseConfig.setDelay(500);

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(getActivity(), "HomeFragment");
        sequence.setConfig(showcaseConfig);

        sequence.addSequenceItem(this.binding.fab,
                "You can add locations which you want to watch from here! Try It!", "Got it");

        sequence.addSequenceItem(getView().findViewById(R.id.calendar_btn),
                "You can see your today's schedule and forecast!", "Got it");

        sequence.start();
    }
}
