package com.mustafizur.hibernateadvanced.application.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.RECORD_COMPONENT})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = UniqueCustomerEmailValidator.class)
public @interface UniqueCustomerEmail {
    String message() default "email is already registered";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
