package com.mustafizur.hibernateadvanced.infrastructure.config;

import com.mustafizur.hibernateadvanced.infrastructure.persistence.*;
import jakarta.persistence.EntityManager;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Component
public class SeedData implements ApplicationRunner {
    private final EntityManager em;

    public SeedData(EntityManager em) {
        this.em = em;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (em.createQuery("select count(c) from CustomerJpaEntity c", Long.class).getSingleResult() > 0) return;
        var customer = new CustomerJpaEntity(UUID.fromString("10000000-0000-0000-0000-000000000001"), "Ada Lovelace", "ada@example.com");
        customer.setAddress(new AddressEmbeddable("1 Analytical Engine Way", "London", "SW1A 1AA", "GB"));
        em.persist(customer);
        em.persist(new ProductJpaEntity(UUID.fromString("20000000-0000-0000-0000-000000000001"), "BOOK-1001", "Hibernate Mastery", new BigDecimal("49.90"), "EUR", 100));
        em.persist(new ProductJpaEntity(UUID.fromString("20000000-0000-0000-0000-000000000002"), "DEV-1002", "Mechanical Keyboard", new BigDecimal("129.00"), "EUR", 40));
        em.persist(new CategoryJpaEntity("BOOKS", "Books"));
        em.persist(new CategoryJpaEntity("DEV", "Developer Equipment"));
    }
}
