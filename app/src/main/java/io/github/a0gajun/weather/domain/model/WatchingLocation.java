/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.domain.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Junya Ogasawara on 1/14/17.
 */

public class WatchingLocation {
    @Getter @Setter private String zipCode;

    @Getter @Setter private int priority;
}
