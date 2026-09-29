package com.mustafizur.hibernateadvanced.infrastructure.config;

import org.hibernate.cfg.AvailableSettings;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.boot.hibernate.autoconfigure.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TenantConfiguration {
    @Bean
    CurrentTenantIdentifierResolver<String> tenantIdentifierResolver() {
        return new CurrentTenantIdentifierResolver<>() {
            @Override public String resolveCurrentTenantIdentifier() { return "demo-tenant"; }
            @Override public boolean validateExistingCurrentSessions() { return true; }
        };
    }

    @Bean
    HibernatePropertiesCustomizer tenantResolverCustomizer(CurrentTenantIdentifierResolver<String> resolver) {
        return properties -> properties.put(AvailableSettings.MULTI_TENANT_IDENTIFIER_RESOLVER, resolver);
    }
}
