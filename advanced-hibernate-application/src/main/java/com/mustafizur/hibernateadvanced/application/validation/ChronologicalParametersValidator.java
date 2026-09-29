package com.mustafizur.hibernateadvanced.application.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.constraintvalidation.SupportedValidationTarget;
import jakarta.validation.constraintvalidation.ValidationTarget;

import java.time.Instant;

@SupportedValidationTarget(ValidationTarget.PARAMETERS)
public final class ChronologicalParametersValidator implements ConstraintValidator<ChronologicalParameters, Object[]> {
    @Override
    public boolean isValid(Object[] value, ConstraintValidatorContext context) {
        if (value == null || value.length < 2 || value[0] == null || value[1] == null) return true;
        return !((Instant) value[0]).isAfter((Instant) value[1]);
    }
}
