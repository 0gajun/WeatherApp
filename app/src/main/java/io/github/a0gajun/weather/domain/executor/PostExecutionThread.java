/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.domain.executor;

import rx.Scheduler;

/**
 * Created by Junya Ogasawara on 1/9/17.
 */

public interface PostExecutionThread {
    Scheduler getScheduler();
}
