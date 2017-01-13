/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.presentation.view;

import io.github.a0gajun.weather.presentation.view.presenter.PermissionPresenter;

/**
 * Created by Junya Ogasawara on 1/12/17.
 */

public interface RequestPermissionView {
    void requestPermission(final String permission, PermissionPresenter.PermissionCode permissionCode);

    void permissionAccepted(PermissionPresenter.PermissionCode permissionCode);

    void permissionDenied(PermissionPresenter.PermissionCode permissionCode);

    void showRationale(PermissionPresenter.PermissionCode permissionCode);
}
