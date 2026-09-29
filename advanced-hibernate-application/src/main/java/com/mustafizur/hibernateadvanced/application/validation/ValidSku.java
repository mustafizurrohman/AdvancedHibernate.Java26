package com.mustafizur.hibernateadvanced.application.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.RECORD_COMPONENT, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = SkuValidator.class)
public @interface ValidSku {
    String message() default "invalid SKU; expected ABC-1234 style";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
