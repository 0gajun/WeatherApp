/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.presentation.di.component;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import io.github.a0gajun.weather.data.net.OpenWeatherMapApi;
import io.github.a0gajun.weather.domain.executor.PostExecutionThread;
import io.github.a0gajun.weather.domain.executor.ThreadExecutor;
import io.github.a0gajun.weather.domain.repository.WeatherRepository;
import io.github.a0gajun.weather.presentation.di.module.ApplicationModule;
import io.github.a0gajun.weather.presentation.di.module.NetModule;
import io.github.a0gajun.weather.presentation.di.module.RepositoryModule;
import io.github.a0gajun.weather.presentation.view.activity.BaseActivity;

/**
 * Created by Junya Ogasawara on 1/9/17.
 */

@Singleton
@Component(modules = {ApplicationModule.class, NetModule.class, RepositoryModule.class})
public interface ApplicationComponent {
    void inject(BaseActivity baseActivity);

    // --- Expose dependee component --- //

    // ApplicationModule
    Application application();

    Context context();

    ThreadExecutor threadExecutor();

    PostExecutionThread postExecutionThread();

    // NetModule
    OpenWeatherMapApi openWeatherMapApi();

    // RepositoryModule
    WeatherRepository weatherRepository();
}
