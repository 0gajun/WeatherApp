/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.data.entity.mapper.util;

import org.junit.Test;

import io.github.a0gajun.weather.ApplicationTestCase;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by Junya Ogasawara on 1/10/17.
 */
public class MapperUtilTest extends ApplicationTestCase {
    @Test
    public void convertCountryCodeToCountryName() throws Exception {
        assertThat(MapperUtil.convertCountryCodeToCountryName("JP"), is("Japan"));
        assertThat(MapperUtil.convertCountryCodeToCountryName("US"), is("United States"));
        assertThat(MapperUtil.convertCountryCodeToCountryName("CN"), is("China"));
    }
}