package com.mustafizur.hibernateadvanced.api;

import com.mustafizur.hibernateadvanced.application.validation.*;
import com.mustafizur.hibernateadvanced.infrastructure.service.ValidationShowcaseService;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import jakarta.validation.constraints.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/validation")
@Validated
public class ValidationController {
    private final ProgrammaticValidationService programmatic;
    private final ValidationShowcaseService methods;
    private final Validator validator;
    public ValidationController(ProgrammaticValidationService programmatic, ValidationShowcaseService methods, Validator validator) {
        this.programmatic=programmatic; this.methods=methods; this.validator=validator;
    }

    @PostMapping("/container-elements")
    Map<String,Object> containerElements(@RequestBody @Valid NewsletterRequest request) { return Map.of("accepted", true, "count", request.recipients().size()); }

    @PostMapping("/date-range")
    DateWindow dateRange(@RequestBody @Valid DateWindow window) { return window; }

    @GetMapping("/method")
    String methodValidation(@RequestParam Instant from, @RequestParam Instant to) { return methods.createReport(from, to); }

    @PostMapping("/programmatic")
    Object programmatic(@RequestBody DateWindow window) { return programmatic.validate(window); }

    @PostMapping("/custom-container")
    BoxRequest customContainer(@RequestBody @Valid BoxRequest request) { return request; }

    @GetMapping("/metadata")
    Object metadata() {
        var descriptor = validator.getConstraintsForClass(DateWindow.class);
        return java.util.Map.of(
            "beanConstrained", descriptor.isBeanConstrained(),
            "classConstraints", descriptor.getConstraintDescriptors().stream().map(d -> d.getAnnotation().annotationType().getSimpleName()).toList(),
            "properties", descriptor.getConstrainedProperties().stream().map(p -> p.getPropertyName()).sorted().toList()
        );
    }

    public record NewsletterRequest(
        @NotEmpty List<@Email String> recipients,
        Map<@NotBlank String, @Size(max=50) String> attributes
    ) { }

    public record BoxRequest(Box<@NotBlank String> value) { }

    @ValidDateRange
    public record DateWindow(@NotNull LocalDate start, @NotNull LocalDate end) implements DateRange { }
}
