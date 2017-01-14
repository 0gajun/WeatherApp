/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.presentation.di.module;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.github.a0gajun.weather.data.entity.OrmaDatabase;

/**
 * Created by Junya Ogasawara on 1/14/17.
 */

@Module
public class DBModule {
    @Provides
    @Singleton
    OrmaDatabase provideOrmaDatabase(Context context) {
        return OrmaDatabase.builder(context).build();
    }
}
