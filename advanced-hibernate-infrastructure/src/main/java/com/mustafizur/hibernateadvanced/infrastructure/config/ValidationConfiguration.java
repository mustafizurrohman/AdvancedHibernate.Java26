package com.mustafizur.hibernateadvanced.infrastructure.config;

import com.mustafizur.hibernateadvanced.application.validation.ProgrammaticValidationService;
import jakarta.validation.Validator;
import org.springframework.boot.validation.autoconfigure.ValidationConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import com.mustafizur.hibernateadvanced.infrastructure.validation.BoxValueExtractor;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ValidationConfiguration {
    @Bean ValidationConfigurationCustomizer valueExtractorCustomizer() {
        return configuration -> configuration.addValueExtractor(new BoxValueExtractor());
    }

    @Bean ProgrammaticValidationService programmaticValidationService(Validator validator) {
        return new ProgrammaticValidationService(validator);
    }
}
