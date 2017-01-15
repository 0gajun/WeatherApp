/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.data.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Getter;

/**
 * Entity class of Google Maps Geocoding API (provided by Google)
 * Documentation is here -> https://developers.google.com/maps/documentation/geocoding/intro
 * <p>
 * Created by Junya Ogasawara on 1/11/17.
 */

public class GoogleMapsGeocodingEntity {

    @Getter @SerializedName("results") private List<Result> results;

    @Getter @SerializedName("status") private String status;


    public static class Result {
        @Getter @SerializedName("address_components") private List<AddressComponent> addressComponents;

        @Getter @SerializedName("formatted_address") private String formattedAddress;

        // currently not used
        // @Getter @SerializedName("geometry") private Geometry geometry;
    }

    public static class AddressComponent {
        @Getter @SerializedName("long_name") private String longName;

        @Getter @SerializedName("short_name") private String shortName;

        @Getter @SerializedName("types") private List<String> types;
    }
}
