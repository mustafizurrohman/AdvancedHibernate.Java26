package com.mustafizur.hibernateadvanced.infrastructure.persistence;

import jakarta.persistence.*;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.NaturalIdCache;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "products")
@NaturalIdCache
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ProductJpaEntity {
    @Id
    private UUID id;
    @Version
    private long version;
    @NaturalId(mutable = false)
    @Column(nullable = false, unique = true, length = 40)
    private String sku;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal price;
    @Column(nullable = false, length = 3)
    private String currency;
    @Column(nullable = false)
    private int stock;

    protected ProductJpaEntity() {
    }

    public ProductJpaEntity(UUID id, String sku, String name, BigDecimal price, String currency, int stock) {
        this.id = id;
        this.sku = sku;
        this.name = name;
        this.price = price;
        this.currency = currency;
        this.stock = stock;
    }

    public UUID getId() {
        return id;
    }

    public String getSku() {
        return sku;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getCurrency() {
        return currency;
    }

    public int getStock() {
        return stock;
    }

    public void reserve(int quantity) {
        if (quantity <= 0 || quantity > stock) throw new IllegalArgumentException("Insufficient stock");
        stock -= quantity;
    }
}
