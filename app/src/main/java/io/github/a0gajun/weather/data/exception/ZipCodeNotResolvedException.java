/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.data.exception;

/**
 * Created by Junya Ogasawara on 1/12/17.
 */

public class ZipCodeNotResolvedException extends RuntimeException {
    public ZipCodeNotResolvedException(String message) {
        super(message);
    }
}
