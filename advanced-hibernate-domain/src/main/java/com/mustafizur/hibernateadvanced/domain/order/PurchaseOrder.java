package com.mustafizur.hibernateadvanced.domain.order;

import com.mustafizur.hibernateadvanced.core.AggregateRoot;
import com.mustafizur.hibernateadvanced.core.DomainException;
import com.mustafizur.hibernateadvanced.domain.common.Money;
import com.mustafizur.hibernateadvanced.domain.customer.CustomerId;
import com.mustafizur.hibernateadvanced.domain.product.ProductId;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

public final class PurchaseOrder extends AggregateRoot {
    private final OrderId id;
    private final CustomerId customerId;
    private final List<OrderLine> lines = new ArrayList<>();
    private OrderStatus status;
    private Instant placedAt;

    public PurchaseOrder(OrderId id, CustomerId customerId) {
        this.id = id;
        this.customerId = customerId;
        this.status = OrderStatus.DRAFT;
    }

    public void addLine(ProductId productId, String sku, String name, int quantity, Money unitPrice) {
        requireDraft();
        lines.add(new OrderLine(productId, sku, name, quantity, unitPrice));
    }

    public void place() {
        requireDraft();
        if (lines.isEmpty()) throw new DomainException("An order requires at least one line");
        status = OrderStatus.PLACED;
        placedAt = Instant.now();
        raise(new OrderPlaced(id, placedAt));
    }

    private void requireDraft() {
        if (status != OrderStatus.DRAFT) throw new DomainException("Order can only be changed while DRAFT");
    }

    public Money total(Currency currency) {
        return lines.stream().map(OrderLine::subtotal).reduce(new Money(java.math.BigDecimal.ZERO, currency), Money::add);
    }

    public OrderId id() {
        return id;
    }

    public CustomerId customerId() {
        return customerId;
    }

    public List<OrderLine> lines() {
        return List.copyOf(lines);
    }

    public OrderStatus status() {
        return status;
    }

    public Instant placedAt() {
        return placedAt;
    }
}
