/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.presentation.view;

import android.content.Context;

/**
 * Created by Junya Ogasawara on 1/14/17.
 */

public interface LoadingView {
    void showLoading();

    void hideLoading();

    void showError(final String errorMsg);

    Context context();
}
