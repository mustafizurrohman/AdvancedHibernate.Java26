package com.mustafizur.hibernateadvanced.domain.order;

import com.mustafizur.hibernateadvanced.domain.common.Money;
import com.mustafizur.hibernateadvanced.domain.product.ProductId;

public record OrderLine(ProductId productId, String sku, String productName, int quantity, Money unitPrice) {
    public OrderLine {
        if (productId == null || sku == null || sku.isBlank() || productName == null || productName.isBlank())
            throw new IllegalArgumentException("Invalid line");
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be positive");
        if (unitPrice == null) throw new IllegalArgumentException("Unit price required");
    }

    public Money subtotal() {
        return unitPrice.multiply(quantity);
    }
}
