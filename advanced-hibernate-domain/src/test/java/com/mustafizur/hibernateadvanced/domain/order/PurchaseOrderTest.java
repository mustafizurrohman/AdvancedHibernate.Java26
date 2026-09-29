package com.mustafizur.hibernateadvanced.domain.order;

import com.mustafizur.hibernateadvanced.domain.common.Money;
import com.mustafizur.hibernateadvanced.domain.customer.CustomerId;
import com.mustafizur.hibernateadvanced.domain.product.ProductId;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.*;

class PurchaseOrderTest {
    @Test
    void placing_requires_lines_and_raises_event() {
        var order = new PurchaseOrder(OrderId.newId(), CustomerId.newId());
        order.addLine(ProductId.newId(), "BOOK-1001", "Hibernate Mastery", 2,
                new Money(new BigDecimal("49.90"), Currency.getInstance("EUR")));
        order.place();
        assertEquals(OrderStatus.PLACED, order.status());
        assertEquals(new BigDecimal("99.80"), order.total(Currency.getInstance("EUR")).amount());
        assertEquals(1, order.pullDomainEvents().size());
    }
}
