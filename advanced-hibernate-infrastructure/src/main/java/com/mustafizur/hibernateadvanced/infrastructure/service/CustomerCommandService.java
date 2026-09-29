package com.mustafizur.hibernateadvanced.infrastructure.service;

import com.mustafizur.hibernateadvanced.application.customer.RegisterCustomerCommand;
import com.mustafizur.hibernateadvanced.infrastructure.persistence.CustomerJpaEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class CustomerCommandService {
    @PersistenceContext private EntityManager em;
    @Transactional
    public UUID register(RegisterCustomerCommand command) {
        var id = UUID.randomUUID();
        em.persist(new CustomerJpaEntity(id, command.name(), command.email()));
        return id;
    }
}
