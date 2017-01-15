/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.domain.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Junya Ogasawara on 1/15/17.
 */

public class ZipCodeUtil {

    private final static Pattern ZIP_CODE_PATTERN = Pattern.compile("\\d{3}-?\\d{4}");

    public static boolean isValidZipCode(final String zipCode) {
        Matcher matcher = ZIP_CODE_PATTERN.matcher(zipCode);
        return matcher.matches();
    }

    public static String formatZipCode(final String zipCode) throws InvalidZipCodeFormatException {
        if (!ZipCodeUtil.isValidZipCode(zipCode)) {
            throw new InvalidZipCodeFormatException(zipCode);
        }

        if (zipCode.length() == 8) { // Already formatted xxx-xxxx (8characters)
            return zipCode;
        }

        return String.format("%s-%s", zipCode.substring(0, 3), zipCode.substring(3, 7));
    }

    public static class InvalidZipCodeFormatException extends RuntimeException {
        public InvalidZipCodeFormatException(final String zipCode) {
            super(String.format("Invalid zip code format : %s", zipCode));
        }
    }

}
