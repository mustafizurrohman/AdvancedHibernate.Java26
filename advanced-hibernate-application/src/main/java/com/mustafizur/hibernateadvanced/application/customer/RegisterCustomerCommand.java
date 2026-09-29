package com.mustafizur.hibernateadvanced.application.customer;

import com.mustafizur.hibernateadvanced.application.validation.UniqueCustomerEmail;
import com.mustafizur.hibernateadvanced.application.validation.ValidationGroups;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterCustomerCommand(
        @NotBlank(groups = ValidationGroups.Basic.class)
        @Size(max = 120, groups = ValidationGroups.Basic.class)
        String name,

        @NotBlank(groups = ValidationGroups.Basic.class)
        @Email(groups = ValidationGroups.Basic.class)
        @UniqueCustomerEmail(groups = ValidationGroups.ExpensiveChecks.class)
        String email
) {
}
