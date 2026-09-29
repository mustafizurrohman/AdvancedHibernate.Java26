package com.mustafizur.hibernateadvanced.infrastructure.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class HibernateConfiguration {
    // Intentionally small: most tuning is explicit in application.yml so the demo is searchable.
    // Production systems should benchmark batch sizes, cache strategy, fetch plans and JDBC settings.
}
