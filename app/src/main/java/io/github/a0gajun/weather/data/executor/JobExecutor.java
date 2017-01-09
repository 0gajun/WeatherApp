/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.data.executor;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import javax.inject.Inject;

import io.github.a0gajun.weather.domain.executor.ThreadExecutor;

/**
 * Created by Junya Ogasawara on 1/9/17.
 */

public class JobExecutor implements ThreadExecutor {

    private final ExecutorService executorService;

    @Inject
    public JobExecutor() {
        this.executorService = Executors.newCachedThreadPool(new JobThreadFactory());
    }

    @Override
    public void execute(Runnable command) {
        if (command == null) {
            throw new IllegalArgumentException("Command cannot be null!!!");
        }

        this.executorService.execute(command);
    }

    private static class JobThreadFactory implements ThreadFactory {
        private static final String THREAD_NAME_PREFIX = "android_0gajun_weather_";
        private int counter = 0;

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, THREAD_NAME_PREFIX + counter++);
        }
    }
}
