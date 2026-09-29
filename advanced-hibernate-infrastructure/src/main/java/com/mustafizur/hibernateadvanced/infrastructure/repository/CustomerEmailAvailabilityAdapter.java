package com.mustafizur.hibernateadvanced.infrastructure.repository;

import com.mustafizur.hibernateadvanced.application.customer.CustomerEmailAvailabilityPort;
import com.mustafizur.hibernateadvanced.infrastructure.persistence.CustomerJpaEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class CustomerEmailAvailabilityAdapter implements CustomerEmailAvailabilityPort {
    @PersistenceContext
    private EntityManager em;

    @Override
    public boolean isAvailable(String email) {
        return em.createQuery("select count(c) from CustomerJpaEntity c where lower(c.email) = lower(:email)", Long.class)
                .setParameter("email", email).getSingleResult() == 0;
    }
}
