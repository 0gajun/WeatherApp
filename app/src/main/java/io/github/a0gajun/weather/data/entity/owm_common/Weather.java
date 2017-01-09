/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.data.entity.owm_common;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;

/**
 * Open Weather Map's common data structure representing Weather.
 *
 * Created by Junya Ogasawara on 1/9/17.
 */

public class Weather {
    @Getter
    @SerializedName("id")
    private int id;

    @Getter
    @SerializedName("main")
    private int main;

    @Getter
    @SerializedName("description")
    private int description;

    @Getter
    @SerializedName("icon")
    private int icon;
}
