package com.mustafizur.hibernateadvanced.infrastructure.persistence;

import jakarta.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;

@Entity
@Table(name = "categories")
@Immutable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class CategoryJpaEntity {
    @Id
    private String code;
    @Column(nullable = false)
    private String name;

    protected CategoryJpaEntity() {
    }

    public CategoryJpaEntity(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
