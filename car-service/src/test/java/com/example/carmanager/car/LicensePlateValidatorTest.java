package com.example.carmanager.car;

import com.example.carmanager.car.domain.policy.LicensePlateValidator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LicensePlateValidatorTest {
    @Test
    void acceptsGenericUppercasePlate() {
        assertThat(LicensePlateValidator.isValid("34 ABC 123")).isTrue();
        assertThat(LicensePlateValidator.isValid("AB-1234")).isTrue();
    }

    @Test
    void rejectsLowercaseOrEdgeSeparators() {
        assertThat(LicensePlateValidator.isValid("34 abc 123")).isFalse();
        assertThat(LicensePlateValidator.isValid("-34 ABC")).isFalse();
        assertThat(LicensePlateValidator.isValid("34 ABC ")).isFalse();
    }
}
