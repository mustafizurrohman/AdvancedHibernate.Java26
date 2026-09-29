package com.mustafizur.hibernateadvanced.core;

import java.time.Instant;

public interface DomainEvent {
    Instant occurredAt();
}
