/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.presentation.view.activity;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.Arrays;

import io.github.a0gajun.weather.AndroidApplication;
import io.github.a0gajun.weather.presentation.di.component.ApplicationComponent;

/**
 * Created by Junya Ogasawara on 1/9/17.
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected void addFragment(final int containerViewId, Fragment fragment) {
        FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
        transaction.add(containerViewId, fragment);
        transaction.commit();
    }

    protected ApplicationComponent getApplicationComponent() {
        return ((AndroidApplication) getApplication()).getApplicationComponent();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        EventBus.getDefault().post(new OnRequestPermissionResultEvent(requestCode, permissions, grantResults));
    }

    public static class OnRequestPermissionResultEvent {
        public final int requestCode;
        public final String[] permissions;
        public final int[] grantResults;

        OnRequestPermissionResultEvent(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            this.requestCode = requestCode;
            this.permissions = Arrays.copyOf(permissions, permissions.length);
            this.grantResults = Arrays.copyOf(grantResults, grantResults.length);
        }
    }
}
