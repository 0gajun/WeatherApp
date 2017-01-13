/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.data.entity.mapper.util;

import org.junit.Test;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import io.github.a0gajun.weather.ApplicationTestCase;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by Junya Ogasawara on 1/10/17.
 */
public class MapperUtilTest extends ApplicationTestCase {

    @Test
    public void convertUnixTimeIntoZonedDateTime() {
        final String str = "2011-12-03T10:15:30+01:00";
        long unixTime = OffsetDateTime.parse(str, DateTimeFormatter.ISO_OFFSET_DATE_TIME).toInstant().getEpochSecond();
        ZonedDateTime result = MapperUtil.convertUnixTimeIntoZonedDateTime(unixTime);

        assertThat(result.getYear(), is(2011));
        assertThat(result.getMonthValue(), is(12));
        assertThat(result.getDayOfMonth(), is(3));
        assertThat(result.getHour(), is(15));
        assertThat(result.getMinute(), is(30));
        assertThat(result.getSecond(), is(30));
    }

    @Test
    public void convertCountryCodeToCountryName() throws Exception {
        assertThat(MapperUtil.convertCountryCodeToCountryName("JP"), is("Japan"));
        assertThat(MapperUtil.convertCountryCodeToCountryName("US"), is("United States"));
        assertThat(MapperUtil.convertCountryCodeToCountryName("CN"), is("China"));
    }
}