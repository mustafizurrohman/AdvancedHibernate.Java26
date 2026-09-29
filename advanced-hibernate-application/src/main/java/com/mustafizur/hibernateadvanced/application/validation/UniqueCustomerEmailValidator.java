package com.mustafizur.hibernateadvanced.application.validation;

import com.mustafizur.hibernateadvanced.application.customer.CustomerEmailAvailabilityPort;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public final class UniqueCustomerEmailValidator implements ConstraintValidator<UniqueCustomerEmail, String> {
    private final CustomerEmailAvailabilityPort port;
    public UniqueCustomerEmailValidator(CustomerEmailAvailabilityPort port) { this.port = port; }
    @Override public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null || value.isBlank() || port.isAvailable(value);
    }
}
