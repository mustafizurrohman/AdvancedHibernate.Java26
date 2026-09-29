package com.mustafizur.hibernateadvanced.infrastructure.service;

import com.mustafizur.hibernateadvanced.application.validation.ChronologicalParameters;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.Instant;

@Service
@Validated
public class ValidationShowcaseService {
    @ChronologicalParameters
    @NotBlank
    public String createReport(@NotNull Instant from, @NotNull Instant to) {
        return "report:" + from + ":" + to;
    }
}
