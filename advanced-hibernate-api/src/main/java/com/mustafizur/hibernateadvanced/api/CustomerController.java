package com.mustafizur.hibernateadvanced.api;

import com.mustafizur.hibernateadvanced.application.customer.RegisterCustomerCommand;
import com.mustafizur.hibernateadvanced.application.validation.ValidationGroups;
import com.mustafizur.hibernateadvanced.infrastructure.service.CustomerCommandService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {
    private final CustomerCommandService service;

    public CustomerController(CustomerCommandService service) {
        this.service = service;
    }

    @PostMapping
    ResponseEntity<Void> register(@RequestBody @Validated(ValidationGroups.OrderedChecks.class) RegisterCustomerCommand command) {
        var id = service.register(command);
        return ResponseEntity.created(URI.create("/api/v1/customers/" + id)).build();
    }
}
