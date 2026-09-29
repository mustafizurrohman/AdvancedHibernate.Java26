package com.mustafizur.hibernateadvanced.infrastructure.repository;

import com.mustafizur.hibernateadvanced.application.report.AnalyticsPort;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Repository
public class AnalyticsAdapter implements AnalyticsPort {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<RunningRevenue> runningRevenue(Instant from) {
        return em.createQuery("""
                        select o.placedAt,
                               o.totalAmount,
                               sum(o.totalAmount) over (order by o.placedAt, o.id)
                        from PurchaseOrderJpaEntity o
                        where o.placedAt >= :from
                        order by o.placedAt, o.id
                        """, Object[].class)
                .setParameter("from", from)
                .getResultList().stream()
                .map(r -> new RunningRevenue((Instant) r[0], (BigDecimal) r[1], (BigDecimal) r[2]))
                .toList();
    }

    @Override
    public List<CustomerRank> topCustomers(int limit) {
        return em.createQuery("""
                        select c.name,
                               sum(o.totalAmount),
                               dense_rank() over (order by sum(o.totalAmount) desc)
                        from PurchaseOrderJpaEntity o join o.customer c
                        group by c.id, c.name
                        order by sum(o.totalAmount) desc
                        """, Object[].class)
                .setMaxResults(limit)
                .getResultList().stream()
                .map(r -> new CustomerRank((String) r[0], (BigDecimal) r[1], ((Number) r[2]).longValue()))
                .toList();
    }
}
