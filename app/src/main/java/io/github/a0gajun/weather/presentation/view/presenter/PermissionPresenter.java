/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.presentation.view.presenter;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Collections;
import java.util.Set;

import javax.inject.Inject;

import io.github.a0gajun.weather.presentation.view.RequestPermissionView;
import lombok.Getter;
import timber.log.Timber;

/**
 * Presenter that requests users to permit several required permission at runtime.
 *
 * Created by Junya Ogasawara on 1/12/17.
 */

public class PermissionPresenter implements Presenter {
    private RequestPermissionView requestPermissionView;

    @Inject
    PermissionPresenter() {
    }

    public void setView(RequestPermissionView requestPermissionView) {
        this.requestPermissionView = requestPermissionView;
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        this.requestPermissionView = null;
    }

    public void requestLocationPermission(Activity activity) {
        Timber.d("Request Location Permission");
        requestPermission(activity, PermissionCode.LOCATION);
    }

    /**
     * Request location permission without showing rationale.
     *
     * @param activity
     */
    public void requestLocationPermissionForcefully(Activity activity) {
        Timber.d("Request Location Permission forcefully");
        ActivityCompat.requestPermissions(activity,
                new String[]{PermissionCode.LOCATION.getPermission()},
                PermissionCode.LOCATION.getRequestCode());
    }

    /**
     * Request location permission without showing rationale.
     *
     * @param activity
     */
    public void requestLocationPermissionWithoutRationale(Activity activity) {
        Timber.d("Request Location Permission forcefully");
        ActivityCompat.requestPermissions(activity,
                new String[]{PermissionCode.LOCATION.getPermission()},
                PermissionCode.LOCATION.getRequestCode());
    }

    /**
     * Permission request is dispatched from Activity.
     * So, you have to handle callback in activity.
     *
     * @param activity
     * @param permissionCode
     */
    private void requestPermission(Activity activity, PermissionCode permissionCode) {
        final String permission = permissionCode.getPermission();
        if (ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED) {
            this.requestPermissionView.permissionAccepted(permissionCode);
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
            this.requestPermissionView.showRationale(permissionCode);
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{permission}, permissionCode.getRequestCode());
        }
    }

    public void checkGrantedPermissions(int[] grantResults, int requestCode) {
        PermissionCode permissionCode = PermissionCode.getFromRequestCode(requestCode);

        if (permissionCode == null) {
            Timber.e("Unknown request code : " + requestCode);
            return;
        }

        if (this.isAllResultGranted(grantResults)) {
            Timber.d("Requested permission is Granted : " + permissionCode.getPermission());
            this.requestPermissionView.permissionAccepted(permissionCode);
        } else {
            Timber.d("Requested permission is Denied : " + permissionCode.getPermission());
            this.requestPermissionView.permissionDenied(permissionCode);
        }
    }

    private boolean isAllResultGranted(int[] grantResults) {
        return Stream.of(ArrayUtils.toObject(grantResults))
                .allMatch(result -> result == PackageManager.PERMISSION_GRANTED);
    }

    public enum PermissionCode {
        LOCATION(Manifest.permission.ACCESS_FINE_LOCATION, 1);

        private static Set<Integer> requestCodeSet = Collections.EMPTY_SET;

        /**
         * Value of {@link android.Manifest.permission}
         */
        @Getter private final String permission;
        /**
         * This value used for request permission's request code
         */
        @Getter private final int requestCode;

        PermissionCode(String permission, int requestCode) {
            this.permission = permission;
            this.requestCode = requestCode;
        }

        @Nullable
        public static PermissionCode getFromRequestCode(final int requestCode) {
            return Stream.of(PermissionCode.values())
                    .filter(v -> v.getRequestCode() == requestCode)
                    .findFirst()
                    .orElse(null);
        }

        public static boolean isHandledRequestCode(final int requestCode) {
            if (requestCodeSet.isEmpty()) {
                requestCodeSet = buildInitialRequestCodeSet();
            }
            return requestCodeSet.contains(requestCode);
        }

        private static Set<Integer> buildInitialRequestCodeSet() {
            return Stream.of(PermissionCode.values())
                    .map(PermissionCode::getRequestCode)
                    .collect(Collectors.toSet());
        }
    }
}
