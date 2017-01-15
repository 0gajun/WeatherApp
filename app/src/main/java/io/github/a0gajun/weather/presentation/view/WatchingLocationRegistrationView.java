/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.presentation.view;

/**
 * Created by Junya Ogasawara on 1/15/17.
 */

public interface WatchingLocationRegistrationView {

    void showLocationInfo(String zipCode, String location);

    void showLocationResolveError();

    void setZipCode(String zipCode);

    void showProgress();

    void onFinishedRegistrationSuccessfully();

    void showError(String msg);
}
