package com.mustafizur.hibernateadvanced.domain.product;

import java.util.UUID;

public record ProductId(UUID value) {
    public ProductId {
        if (value == null) throw new IllegalArgumentException("ProductId cannot be null");
    }

    public static ProductId newId() {
        return new ProductId(UUID.randomUUID());
    }
}
