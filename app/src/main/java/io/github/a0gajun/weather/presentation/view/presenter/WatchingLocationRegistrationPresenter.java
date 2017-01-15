/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.presentation.view.presenter;

import android.content.Context;

import javax.inject.Inject;

import io.github.a0gajun.weather.R;
import io.github.a0gajun.weather.domain.model.GeocodingResult;
import io.github.a0gajun.weather.domain.model.WatchingLocation;
import io.github.a0gajun.weather.domain.usecase.DefaultSubscriber;
import io.github.a0gajun.weather.domain.usecase.GeocodingUsingZipCode;
import io.github.a0gajun.weather.domain.usecase.RegisterWatchingLocation;
import io.github.a0gajun.weather.domain.util.ZipCodeUtil;
import io.github.a0gajun.weather.presentation.view.WatchingLocationRegistrationView;
import timber.log.Timber;

/**
 * Created by Junya Ogasawara on 1/15/17.
 */

public class WatchingLocationRegistrationPresenter implements Presenter {

    private final Context context;
    private final RegisterWatchingLocation registerWatchingLocation;
    private final GeocodingUsingZipCode geocodingUsingZipCode;

    private WatchingLocationRegistrationView watchingLocationRegistrationView;

    @Inject
    public WatchingLocationRegistrationPresenter(Context context,
                                                 GeocodingUsingZipCode geocodingUsingZipCode,
                                                 RegisterWatchingLocation registerWatchingLocation) {
        this.context = context;
        this.registerWatchingLocation = registerWatchingLocation;
        this.geocodingUsingZipCode = geocodingUsingZipCode;
    }

    public void setView(WatchingLocationRegistrationView watchingLocationRegistrationView) {
        this.watchingLocationRegistrationView = watchingLocationRegistrationView;
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        this.registerWatchingLocation.unsubscribe();
        this.geocodingUsingZipCode.unsubscribe();
    }

    public void registerWatchingLocation(String zipCode) {
        if (!ZipCodeUtil.isValidZipCode(zipCode)) {
            this.watchingLocationRegistrationView.showError(this.context.getString(R.string.msg_postalcode_fmt_is_invalid));
            return;
        }
        zipCode = ZipCodeUtil.formatZipCode(zipCode);
        this.watchingLocationRegistrationView.showProgress();

        WatchingLocation watchingLocation = new WatchingLocation();
        watchingLocation.setPriority(WatchingLocation.DEFAULT_PRIORITY);
        watchingLocation.setZipCode(zipCode);

        this.registerWatchingLocation.setWatchingLocation(watchingLocation);
        this.registerWatchingLocation.execute(new RegisterWatchingLocationSubscriber());
    }

    public void resolveLocationByZipCode(String zipCode) {
        if (!ZipCodeUtil.isValidZipCode(zipCode)) {
            return;
        }
        this.geocodingUsingZipCode.setZipCode(zipCode);
        this.geocodingUsingZipCode.execute(new GeocodingUsingZipCodeSubscriber());
    }

    private class RegisterWatchingLocationSubscriber extends DefaultSubscriber<WatchingLocation> {
        @Override
        public void onError(Throwable e) {
            super.onError(e);
            if (context == null) {
                return;
            }

            final String errMsg = WatchingLocationRegistrationPresenter.this
                    .context.getString(R.string.msg_postalcode_already_registered);
            WatchingLocationRegistrationPresenter.this
                    .watchingLocationRegistrationView.showError(errMsg);
        }

        @Override
        public void onNext(WatchingLocation watchingLocation) {
            super.onNext(watchingLocation);
            WatchingLocationRegistrationPresenter.this
                    .watchingLocationRegistrationView.onFinishedRegistrationSuccessfully();
        }
    }

    private class GeocodingUsingZipCodeSubscriber extends DefaultSubscriber<GeocodingResult> {
        @Override
        public void onError(Throwable e) {
            super.onError(e);
            Timber.e(e);
            WatchingLocationRegistrationPresenter.this
                    .watchingLocationRegistrationView.showLocationResolveError();
        }

        @Override
        public void onNext(GeocodingResult result) {
            super.onNext(result);
            final String location = String.format("%s, %s", result.getCityName(), result.getCountryName());
            WatchingLocationRegistrationPresenter.this
                    .watchingLocationRegistrationView.showLocationInfo(result.getPostalCode(), location);
        }
    }
}
