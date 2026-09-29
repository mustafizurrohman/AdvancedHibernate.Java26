package com.mustafizur.hibernateadvanced.application.validation;

import com.mustafizur.hibernateadvanced.application.order.CreateOrderCommand;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public final class ConsistentOrderRequestValidator implements ConstraintValidator<ConsistentOrderRequest, CreateOrderCommand> {
    @Override public boolean isValid(CreateOrderCommand value, ConstraintValidatorContext context) {
        if (value == null || value.lines() == null) return true;
        var currencies = value.lines().stream().map(CreateOrderCommand.Line::currency).filter(java.util.Objects::nonNull).distinct().count();
        if (currencies <= 1) return true;
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate("all order lines must use the same currency")
            .addPropertyNode("lines").addConstraintViolation();
        return false;
    }
}
