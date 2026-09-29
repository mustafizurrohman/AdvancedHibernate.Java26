package com.mustafizur.hibernateadvanced.infrastructure.persistence.payment;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "payments")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "payment_type")
public abstract class PaymentJpaEntity {
    @Id
    protected UUID id;
    @Column(nullable = false)
    protected UUID orderId;
    @Column(nullable = false, precision = 19, scale = 4)
    protected BigDecimal amount;
    @Column(nullable = false, length = 3)
    protected String currency;

    protected PaymentJpaEntity() {
    }

    protected PaymentJpaEntity(UUID id, UUID orderId, BigDecimal amount, String currency) {
        this.id = id;
        this.orderId = orderId;
        this.amount = amount;
        this.currency = currency;
    }
}
