package com.mustafizur.hibernateadvanced.application.order;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record OrderDetails(UUID id, String customerName, String status, Instant placedAt, List<Line> lines) {
    public record Line(String sku, String name, int quantity, BigDecimal unitPrice, String currency) {
    }
}
