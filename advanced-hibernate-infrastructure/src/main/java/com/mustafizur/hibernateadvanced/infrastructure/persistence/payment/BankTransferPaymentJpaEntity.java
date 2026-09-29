package com.mustafizur.hibernateadvanced.infrastructure.persistence.payment;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@DiscriminatorValue("BANK")
public class BankTransferPaymentJpaEntity extends PaymentJpaEntity {
    private String reference;

    protected BankTransferPaymentJpaEntity() {
    }

    public BankTransferPaymentJpaEntity(UUID id, UUID orderId, BigDecimal amount, String currency, String reference) {
        super(id, orderId, amount, currency);
        this.reference = reference;
    }
}
