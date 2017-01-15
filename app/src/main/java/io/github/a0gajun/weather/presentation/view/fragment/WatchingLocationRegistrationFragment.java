/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.presentation.view.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.common.eventbus.Subscribe;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.github.a0gajun.weather.R;
import io.github.a0gajun.weather.databinding.FragmentWatchingLocationRegistrationBinding;
import io.github.a0gajun.weather.domain.model.WatchingLocation;
import io.github.a0gajun.weather.domain.util.ZipCodeUtil;
import io.github.a0gajun.weather.presentation.di.component.ActivityComponent;
import io.github.a0gajun.weather.presentation.view.WatchingLocationRegistrationView;
import io.github.a0gajun.weather.presentation.view.activity.BaseActivity;
import io.github.a0gajun.weather.presentation.view.activity.HomeActivity;
import io.github.a0gajun.weather.presentation.view.presenter.WatchingLocationRegistrationPresenter;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

/**
 * Created by Junya Ogasawara on 1/15/17.
 */

public class WatchingLocationRegistrationFragment extends BaseFragment
        implements WatchingLocationRegistrationView {

    @Inject WatchingLocationRegistrationPresenter watchingLocationRegistrationPresenter;

    private FragmentWatchingLocationRegistrationBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_watching_location_registration, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.getComponent(ActivityComponent.class).inject(this);
        this.binding = FragmentWatchingLocationRegistrationBinding.bind(getView());

        this.setUpToolbar();
        this.setUpRegisterButton();
        this.setUpEditText();

        this.watchingLocationRegistrationPresenter.setView(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        this.watchingLocationRegistrationPresenter.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.watchingLocationRegistrationPresenter.pause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.watchingLocationRegistrationPresenter.destroy();
    }

    private void setUpToolbar() {
        this.binding.toolbarBinding.toolbar.inflateMenu(R.menu.menu_search);
        this.binding.toolbarBinding.toolbar.setNavigationIcon(R.drawable.ic_navigate_before_white_24dp);
        this.binding.toolbarBinding.toolbar.setNavigationOnClickListener(v -> EventBus.getDefault().post(new BaseActivity.FinishActivityEvent()));

        MenuItem menuItem = binding.toolbarBinding.toolbar.getMenu().findItem(R.id.search_view);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search with location name");
        searchView.setIconifiedByDefault(false);
    }

    private void setUpRegisterButton() {
        binding.registerBtn.setOnClickListener(v -> {
            final String zipCode = this.binding.postalCodeEditText.getText().toString();
            this.watchingLocationRegistrationPresenter.registerWatchingLocation(zipCode);
        });
    }

    private void setUpEditText() {
        binding.postalCodeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                final String text = s.toString();
                // TODO: Move to presenter?
                if (ZipCodeUtil.isValidZipCode(text)) {
                    WatchingLocationRegistrationFragment.this
                            .showNormalMsgInLocationCard(R.string.msg_searching);
                    WatchingLocationRegistrationFragment.this
                            .watchingLocationRegistrationPresenter.resolveLocationByZipCode(text);
                } else {
                    // TODO: Reduce call count
                    WatchingLocationRegistrationFragment.this
                            .showNormalMsgInLocationCard(R.string.msg_please_input_postal_code);
                }
            }
        });
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void onFinishedRegistrationSuccessfully() {
        binding.registerBtn.setEnabled(false);
        Snackbar.make(getView(), R.string.msg_successfully_registered, Snackbar.LENGTH_SHORT).show();

        // Delay for showing snack bar...
        Observable.timer(1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(l -> EventBus.getDefault().post(new BaseActivity.FinishActivityEvent(Activity.RESULT_OK)));
    }

    @Override
    public void showError(String msg) {
        Snackbar.make(getView(), msg, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showLocationResolveError() {
        this.showErrorMsgInLocationCard(R.string.msg_your_input_location_not_resolved);

        // Disable because postal code is valid!
        this.binding.registerBtn.setEnabled(false);
    }

    @Override
    public void showLocationInfo(String zipCode, String location) {
        this.binding.resolvedLocationLabel.setVisibility(View.VISIBLE);
        this.binding.postalCode.setVisibility(View.VISIBLE);
        this.binding.address.setVisibility(View.VISIBLE);
        this.binding.msgLocationCard.setVisibility(View.GONE);
        this.binding.postalCode.setText(getString(R.string.postal_code_fmt, zipCode));
        this.binding.address.setText(location);

        // Enable because postal code is valid!
        this.binding.registerBtn.setEnabled(true);
    }

    private void showErrorMsgInLocationCard(@StringRes int resId) {
        this.showMsgInLocationCard(resId, R.color.holo_red_light);
    }

    private void showNormalMsgInLocationCard(@StringRes int resId) {
        this.showMsgInLocationCard(resId, R.color.defaultColorText);
    }

    private void showMsgInLocationCard(@StringRes int resId, @ColorRes int colorResId) {
        this.binding.resolvedLocationLabel.setVisibility(View.GONE);
        this.binding.postalCode.setVisibility(View.GONE);
        this.binding.address.setVisibility(View.GONE);
        this.binding.msgLocationCard.setVisibility(View.VISIBLE);
        this.binding.msgLocationCard.setText(resId);
        this.binding.msgLocationCard.setTextColor(getResources().getColor(colorResId));
    }
}
