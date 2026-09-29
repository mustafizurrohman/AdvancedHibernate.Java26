package com.mustafizur.hibernateadvanced.infrastructure.persistence;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnTransformer;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "temperature_readings")
public class TemperatureReadingJpaEntity {
    @Id
    private UUID id;

    @Column(name = "temperature_kelvin", nullable = false, precision = 8, scale = 3)
    @ColumnTransformer(read = "temperature_kelvin - 273.15", write = "? + 273.15")
    private BigDecimal temperatureCelsius;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;
    @Column(nullable = false)
    private Instant modifiedAt;

    protected TemperatureReadingJpaEntity() {
    }

    public TemperatureReadingJpaEntity(UUID id, BigDecimal temperatureCelsius) {
        this.id = id;
        this.temperatureCelsius = temperatureCelsius;
    }

    @PrePersist
    void onCreate() {
        createdAt = modifiedAt = Instant.now();
    }

    @PreUpdate
    void onUpdate() {
        modifiedAt = Instant.now();
    }

    public BigDecimal getTemperatureCelsius() {
        return temperatureCelsius;
    }

    public void setTemperatureCelsius(BigDecimal value) {
        this.temperatureCelsius = value;
    }
}
