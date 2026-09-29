package com.mustafizur.hibernateadvanced.application.validation;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import java.util.Set;

public final class ProgrammaticValidationService {
    private final Validator validator;
    public ProgrammaticValidationService(Validator validator) { this.validator = validator; }

    public <T> T validate(T value, Class<?>... groups) {
        var violations = validator.validate(value, groups);
        if (!violations.isEmpty()) throw new ConstraintViolationException(Set.copyOf(violations));
        return value;
    }
}
