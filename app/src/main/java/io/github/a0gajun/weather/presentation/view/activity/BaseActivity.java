/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.presentation.view.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Arrays;

import io.github.a0gajun.weather.AndroidApplication;
import io.github.a0gajun.weather.presentation.di.component.ApplicationComponent;

/**
 * Created by Junya Ogasawara on 1/9/17.
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

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

    @Subscribe
    public void onNavigationEvent(NavigationEvent navigationEvent) {
        if (navigationEvent.needsResult) {
            startActivityForResult(navigationEvent.intent, navigationEvent.requestCode);
        } else {
            startActivity(navigationEvent.intent);
        }
    }

    @Subscribe
    public void onFinishActivityEvent(FinishActivityEvent finishActivityEvent) {
        finish();
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

    public static class NavigationEvent {
        final Intent intent;
        final boolean needsResult;
        final int requestCode;

        public NavigationEvent(Intent intent) {
            this(intent, false, 0);
        }

        public NavigationEvent(Intent intent, int requestCode) {
            this(intent, true, requestCode);
        }

        private NavigationEvent(Intent intent, boolean needsResult, int requestCode) {
            this.intent = intent;
            this.needsResult = needsResult;
            this.requestCode = requestCode;
        }
    }

    public static class FinishActivityEvent {
    }
}
