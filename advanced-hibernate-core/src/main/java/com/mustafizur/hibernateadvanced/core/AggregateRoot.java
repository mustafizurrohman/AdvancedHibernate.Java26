package com.mustafizur.hibernateadvanced.core;

import java.util.ArrayList;
import java.util.List;

public abstract class AggregateRoot {
    private final List<DomainEvent> domainEvents = new ArrayList<>();

    protected final void raise(DomainEvent event) {
        domainEvents.add(event);
    }

    public final List<DomainEvent> pullDomainEvents() {
        var copy = List.copyOf(domainEvents);
        domainEvents.clear();
        return copy;
    }
}
