package com.mustafizur.hibernateadvanced.application.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ConsistentOrderRequestValidator.class)
public @interface ConsistentOrderRequest {
    String message() default "order request is inconsistent";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
