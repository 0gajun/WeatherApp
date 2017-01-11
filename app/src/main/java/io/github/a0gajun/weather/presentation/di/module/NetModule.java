/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.presentation.di.module;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.github.a0gajun.weather.BuildConfig;
import io.github.a0gajun.weather.data.net.OpenWeatherMapApi;
import okhttp3.Cache;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static io.github.a0gajun.weather.presentation.di.module.Qualifiers.OPEN_WHETHER_MAP_API;

/**
 * Created by Junya Ogasawara on 1/9/17.
 */

@Module
public class NetModule {
    private static final int CACHE_SIZE = 20 * 1024 * 1024; // 20MiB

    private static final String OPEN_WEATHER_MAP_API_ENDPOINT = "http://api.openweathermap.org";

    @Singleton
    @Provides
    Cache provideCache(Application application) {
        return new Cache(application.getCacheDir(), CACHE_SIZE);
    }

    @Singleton
    @Provides
    Gson provideGson() {
        return new GsonBuilder()
                .create();
    }

    @Singleton
    @Provides
    OkHttpClient provideOkHttpClient(Cache cache) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request request = chain.request();
                    final HttpUrl url = request.url().newBuilder().addQueryParameter("APPID", BuildConfig.OPEN_WEATHER_MAP_API_KEY).build();
                    request = request.newBuilder().url(url).build();
                    return chain.proceed(request);
                })
                .addInterceptor(loggingInterceptor)
                .cache(cache)
                .build();
    }

    @Singleton
    @Provides
    @Named(OPEN_WHETHER_MAP_API)
    Retrofit provideOpenWeatherMapRetrofit(Gson gson, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(OPEN_WEATHER_MAP_API_ENDPOINT)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();
    }

    @Provides
    @Singleton
    OpenWeatherMapApi provideOpenWeatherApi(@Named(OPEN_WHETHER_MAP_API) Retrofit retrofit) {
        return retrofit.create(OpenWeatherMapApi.class);
    }
}
