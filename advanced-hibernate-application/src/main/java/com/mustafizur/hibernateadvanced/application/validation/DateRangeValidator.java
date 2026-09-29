package com.mustafizur.hibernateadvanced.application.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public final class DateRangeValidator implements ConstraintValidator<ValidDateRange, DateRange> {
    @Override
    public boolean isValid(DateRange value, ConstraintValidatorContext context) {
        if (value == null || value.start() == null || value.end() == null) return true;
        if (!value.start().isAfter(value.end())) return true;
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                .addPropertyNode("end").addConstraintViolation();
        return false;
    }
}
