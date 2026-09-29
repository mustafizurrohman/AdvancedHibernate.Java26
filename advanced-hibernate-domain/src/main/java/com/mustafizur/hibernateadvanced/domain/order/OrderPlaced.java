package com.mustafizur.hibernateadvanced.domain.order;

import com.mustafizur.hibernateadvanced.core.DomainEvent;
import java.time.Instant;

public record OrderPlaced(OrderId orderId, Instant occurredAt) implements DomainEvent { }
