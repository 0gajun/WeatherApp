/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.data.entity.mapper;

import android.support.annotation.Nullable;

import javax.inject.Inject;

import io.github.a0gajun.weather.data.entity.WatchingLocationEntity;
import io.github.a0gajun.weather.domain.model.WatchingLocation;

/**
 * Created by Junya Ogasawara on 1/14/17.
 */

public class WatchingLocationMapper {

    @Inject
    public WatchingLocationMapper() {
    }

    @Nullable
    public WatchingLocation transform(WatchingLocationEntity entity) {
        if (entity == null) {
            return null;
        }

        WatchingLocation model = new WatchingLocation();
        model.setZipCode(entity.getZipCode());
        model.setPriority(entity.getPriority());

        return model;
    }

    @Nullable
    public WatchingLocationEntity transform(WatchingLocation model) {
        if (model == null) {
            return null;
        }

        WatchingLocationEntity entity = new WatchingLocationEntity();
        entity.setZipCode(model.getZipCode());
        entity.setPriority(model.getPriority());

        return entity;
    }
}
