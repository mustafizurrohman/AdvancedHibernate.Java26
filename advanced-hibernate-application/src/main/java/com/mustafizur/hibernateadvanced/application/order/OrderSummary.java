package com.mustafizur.hibernateadvanced.application.order;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record OrderSummary(UUID id, String customerName, String status, BigDecimal total, String currency,
                           Instant placedAt) {
}
