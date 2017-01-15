/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.data.repository;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.net.ConnectException;

import javax.inject.Inject;

import io.github.a0gajun.weather.domain.repository.LocationRepository;
import rx.Observable;
import rx.Subscriber;
import rx.subscriptions.Subscriptions;
import timber.log.Timber;

/**
 * When you use this repository, you have to get location permission before.
 * <p>
 * Created by Junya Ogasawara on 1/12/17.
 */

public class LocationRepositoryImpl implements LocationRepository,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private static final long LOCATION_UPDATE_INTERVAL = 10000; // 10s
    private static final long LOCATION_UPDATE_FASTEST_INTERVAL = 2000; // 2s

    private final Context context;

    private GoogleApiClient googleApiClient;
    private Subscriber<? super Location> subscriber;
    private Location currentLocation;


    @Inject
    public LocationRepositoryImpl(Context context) {
        this.context = context;
    }

    @Override
    public Observable<Location> startLocationUpdates() {
        return Observable.create(subscriber -> {
            Timber.d("Subscriber");
            this.subscriber = subscriber;
            this.googleApiClient = buildGoogleApiClient();
            this.googleApiClient.connect();

            subscriber.add(Subscriptions.create(() -> {
                // Release resources after unsubscribed subscriber
                Timber.d("Tear down...");
                if (this.googleApiClient.isConnected() || this.googleApiClient.isConnecting()) {
                    LocationServices.FusedLocationApi.removeLocationUpdates(this.googleApiClient, this);
                    this.googleApiClient.disconnect();
                    this.subscriber = null;
                }
            }));

        });
    }

    private GoogleApiClient buildGoogleApiClient() {
        return new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    public void stopLocationUpdates() {
        if (this.subscriber != null && this.subscriber.isUnsubscribed()) {
            this.subscriber.unsubscribe();
            this.subscriber = null;
        }
    }

    @Nullable
    @Override
    public Location getCurrentLocation() {
        return this.currentLocation;
    }

    private void setCurrentLocation(Location location) {
        this.currentLocation = location;
    }

    @Override
    @SuppressWarnings("MissingPermission")
    public void onConnected(@Nullable Bundle bundle) {
        Timber.d("onConnected");

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        Location currentLocation = LocationServices.FusedLocationApi.getLastLocation(this.googleApiClient);

        if (currentLocation != null) {
            this.subscriber.onNext(currentLocation);
        }

        requestLocationUpdate();
    }

    @SuppressWarnings("MissingPermission")
    private void requestLocationUpdate() {
        Timber.d("requestLocationUpdate");
        LocationServices.FusedLocationApi
                .requestLocationUpdates(this.googleApiClient, buildLocationRequest(), this);
    }

    private LocationRequest buildLocationRequest() {
        return new LocationRequest()
                .setInterval(LOCATION_UPDATE_INTERVAL)
                .setFastestInterval(LOCATION_UPDATE_FASTEST_INTERVAL)
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Timber.d("onConnectionSuspended");
        this.subscriber.onError(new ConnectException("Couldn't connect to Google Api Client"));
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Timber.d("onConnectionFailed");
        this.subscriber.onError(new ConnectException("Couldn't connect to Google Api Client"));
    }

    @Override
    public void onLocationChanged(Location location) {
        Timber.d("onLocationChanged");
        setCurrentLocation(location);
        this.subscriber.onNext(location);
    }
}
