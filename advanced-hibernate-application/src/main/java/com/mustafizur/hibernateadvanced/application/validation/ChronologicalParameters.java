package com.mustafizur.hibernateadvanced.application.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraintvalidation.SupportedValidationTarget;
import jakarta.validation.constraintvalidation.ValidationTarget;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ChronologicalParametersValidator.class)
public @interface ChronologicalParameters {
    String message() default "first instant must not be after second instant";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
