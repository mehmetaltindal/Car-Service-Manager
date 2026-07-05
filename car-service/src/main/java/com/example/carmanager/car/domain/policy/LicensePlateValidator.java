package com.example.carmanager.car.domain.policy;

import java.util.regex.Pattern;

public final class LicensePlateValidator {
    private static final Pattern PATTERN = Pattern.compile("^[A-Z0-9][A-Z0-9 -]{1,15}[A-Z0-9]$");

    private LicensePlateValidator() {
    }

    public static boolean isValid(String value) {
        return value != null && PATTERN.matcher(value).matches();
    }
}
