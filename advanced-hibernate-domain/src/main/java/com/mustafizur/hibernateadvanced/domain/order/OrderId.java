package com.mustafizur.hibernateadvanced.domain.order;

import java.util.UUID;

public record OrderId(UUID value) {
    public OrderId {
        if (value == null) throw new IllegalArgumentException("OrderId cannot be null");
    }

    public static OrderId newId() {
        return new OrderId(UUID.randomUUID());
    }
}
