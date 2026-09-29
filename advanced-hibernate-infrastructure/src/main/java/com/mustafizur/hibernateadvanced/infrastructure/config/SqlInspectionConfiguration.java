package com.mustafizur.hibernateadvanced.infrastructure.config;

import org.hibernate.cfg.AvailableSettings;
import org.hibernate.resource.jdbc.spi.StatementInspector;
import org.slf4j.LoggerFactory;
import org.springframework.boot.hibernate.autoconfigure.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SqlInspectionConfiguration {
    @Bean StatementInspector statementInspector() {
        var log = LoggerFactory.getLogger("hibernate.sql.inspector");
        return sql -> { log.debug("SQL inspector: {}", sql); return sql; };
    }

    @Bean HibernatePropertiesCustomizer statementInspectorCustomizer(StatementInspector inspector) {
        return props -> props.put(AvailableSettings.STATEMENT_INSPECTOR, inspector);
    }
}
