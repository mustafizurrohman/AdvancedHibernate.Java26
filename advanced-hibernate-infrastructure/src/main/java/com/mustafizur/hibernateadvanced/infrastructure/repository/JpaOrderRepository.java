package com.mustafizur.hibernateadvanced.infrastructure.repository;

import com.mustafizur.hibernateadvanced.application.order.*;
import com.mustafizur.hibernateadvanced.domain.order.PurchaseOrder;
import com.mustafizur.hibernateadvanced.infrastructure.persistence.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class JpaOrderRepository implements OrderRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    public void save(PurchaseOrder order) {
        var customer = em.getReference(CustomerJpaEntity.class, order.customerId().value());
        var currency = order.lines().getFirst().unitPrice().currency();
        var entity = new PurchaseOrderJpaEntity(order.id().value(), customer, order.status(), order.placedAt(), order.total(currency).amount(), currency.getCurrencyCode());
        for (var line : order.lines()) {
            var product = em.getReference(ProductJpaEntity.class, line.productId().value());
            entity.addLine(new OrderLineJpaEntity(entity, product, line.sku(), line.productName(), line.quantity(), line.unitPrice().amount(), line.unitPrice().currency().getCurrencyCode()));
        }
        em.persist(entity);
    }

    @Override
    public Optional<OrderDetails> findDetails(UUID orderId) {
        var graph = em.getEntityGraph("Order.details");
        return em.createQuery("select o from PurchaseOrderJpaEntity o where o.id = :id", PurchaseOrderJpaEntity.class)
                .setParameter("id", orderId)
                .setHint("jakarta.persistence.fetchgraph", graph)
                .getResultStream()
                .findFirst()
                .map(this::toDetails);
    }

    @Override
    public List<OrderSummary> findSummaries(int page, int size) {
        return em.createQuery("""
                        select o.id, o.customer.name, o.status, o.totalAmount, o.currency, o.placedAt
                        from PurchaseOrderJpaEntity o
                        order by o.placedAt desc, o.id desc
                        """, Object[].class)
                .setFirstResult(page * size)
                .setMaxResults(size)
                .getResultList().stream()
                .map(r -> new OrderSummary((UUID) r[0], (String) r[1], r[2].toString(), (java.math.BigDecimal) r[3], (String) r[4], (java.time.Instant) r[5]))
                .toList();
    }

    private OrderDetails toDetails(PurchaseOrderJpaEntity o) {
        var lines = o.getLines().stream().map(l -> new OrderDetails.Line(l.getSku(), l.getProductName(), l.getQuantity(), l.getUnitPrice(), l.getCurrency())).toList();
        return new OrderDetails(o.getId(), o.getCustomer().getName(), o.getStatus().name(), o.getPlacedAt(), lines);
    }
}
