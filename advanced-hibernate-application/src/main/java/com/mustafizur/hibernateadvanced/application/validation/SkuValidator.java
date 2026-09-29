package com.mustafizur.hibernateadvanced.application.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public final class SkuValidator implements ConstraintValidator<ValidSku, String> {
    private static final Pattern PATTERN = Pattern.compile("^[A-Z]{3,10}-[0-9]{4,12}$");
    @Override public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null || PATTERN.matcher(value).matches();
    }
}
