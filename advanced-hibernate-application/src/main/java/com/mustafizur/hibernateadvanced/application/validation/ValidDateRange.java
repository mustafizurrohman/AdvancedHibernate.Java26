package com.mustafizur.hibernateadvanced.application.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = DateRangeValidator.class)
public @interface ValidDateRange {
    String message() default "start must be before or equal to end";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
