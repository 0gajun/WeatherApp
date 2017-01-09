/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.presentation.view.presenter;

/**
 * Interface representing a Presenter.
 *
 * Created by Junya Ogasawara on 1/9/17.
 */
public interface Presenter {
    void resume();

    void pause();

    void destroy();
}