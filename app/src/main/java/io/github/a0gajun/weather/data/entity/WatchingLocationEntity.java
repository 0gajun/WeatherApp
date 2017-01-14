/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.data.entity;

import com.github.gfx.android.orma.annotation.Column;
import com.github.gfx.android.orma.annotation.PrimaryKey;
import com.github.gfx.android.orma.annotation.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Junya Ogasawara on 1/14/17.
 */

@Table
public class WatchingLocationEntity {
    @PrimaryKey
    @Getter @Setter long id;

    @Column
    @Getter @Setter int priority;

    @Column
    @Getter @Setter String zipCode;

}
