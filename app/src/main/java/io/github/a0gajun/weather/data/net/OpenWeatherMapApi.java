/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.data.net;

import io.github.a0gajun.weather.data.entity.CurrentWeatherDataEntity;
import io.github.a0gajun.weather.data.entity.FiveDayWeatherForecastDataEntity;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Retrofit interface definition for OpenWeatherMap Api.
 * (http://openweathermap.org/)
 * <p>
 * Created by Junya Ogasawara on 1/9/17.
 */

public interface OpenWeatherMapApi {

    /**
     * Retrieve current weather data for one location
     *
     * @param cityNameCountryCode city name and country code divided by comma, use ISO 3166 country codes
     * @return
     */
    @GET("data/2.5/weather?units=metric")
    Observable<CurrentWeatherDataEntity> getCurrentWeather(@Query("q") String cityNameCountryCode);

    /**
     * Retrieve current weather data for one location using zip code.
     *
     * @param zipCountryCode zip code and country code divided by comma
     * @return
     */
    @GET("data/2.5/weather?units=metric")
    Observable<CurrentWeatherDataEntity> getCurrentWeatherByZipCode(@Query("zip") String zipCountryCode);

    /**
     * Retrieve 5 day weather forecast.
     *
     * @param cityNameCountryCode city name and country code divided by comma, use ISO 3166 country codes
     * @return
     */
    @GET("data/2.5/forecast?units=metric")
    Observable<FiveDayWeatherForecastDataEntity> getFiveDayWeatherForecast(@Query("q") String cityNameCountryCode);

    /**
     * Retrieve 5 day weather data for one location using zip code.
     *
     * @param zipCountryCode zip code and country code divided by comma
     * @return
     */
    @GET("data/2.5/forecast?units=metric")
    Observable<FiveDayWeatherForecastDataEntity> getFiveDayWeatherForecastByZipCode(@Query("zip") String zipCountryCode);
}