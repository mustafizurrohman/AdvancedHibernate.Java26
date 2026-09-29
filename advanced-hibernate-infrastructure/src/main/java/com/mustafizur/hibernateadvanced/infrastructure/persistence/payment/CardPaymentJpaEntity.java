package com.mustafizur.hibernateadvanced.infrastructure.persistence.payment;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@DiscriminatorValue("CARD")
public class CardPaymentJpaEntity extends PaymentJpaEntity {
    private String cardNetwork;
    private String maskedPan;
    protected CardPaymentJpaEntity() { }
    public CardPaymentJpaEntity(UUID id, UUID orderId, BigDecimal amount, String currency, String cardNetwork, String maskedPan) {
        super(id, orderId, amount, currency); this.cardNetwork=cardNetwork; this.maskedPan=maskedPan;
    }
}
