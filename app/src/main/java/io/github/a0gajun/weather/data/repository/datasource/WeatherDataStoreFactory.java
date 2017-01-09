/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.data.repository.datasource;

import javax.inject.Inject;

import io.github.a0gajun.weather.data.net.OpenWeatherMapApi;

/**
 * Created by Junya Ogasawara on 1/9/17.
 */

public class WeatherDataStoreFactory {

    private final OpenWeatherMapApi openWeatherMapApi;

    @Inject
    public WeatherDataStoreFactory(OpenWeatherMapApi openWeatherMapApi) {
        this.openWeatherMapApi = openWeatherMapApi;
    }

    public WeatherDataStore create() {
        return createCloudDataStore();
    }

    private CloudWeatherDataStore createCloudDataStore() {
        return new CloudWeatherDataStore(this.openWeatherMapApi);
    }
}
