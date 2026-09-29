package com.mustafizur.hibernateadvanced.infrastructure.persistence;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "customers", indexes = @Index(name = "ix_customer_email", columnList = "email", unique = true))
@SoftDelete(columnName = "deleted")
@NamedEntityGraph(name = "Customer.withOrders", attributeNodes = @NamedAttributeNode("orders"))
public class CustomerJpaEntity {
    @Id
    private UUID id;
    @Version
    private long version;
    @Column(nullable = false, length = 120)
    private String name;
    @Column(nullable = false, unique = true, length = 320)
    private String email;
    @Embedded
    private AddressEmbeddable address;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private CustomerPreferences preferences;
    @Column(nullable = false)
    private Instant createdAt;

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    @org.hibernate.annotations.Fetch(org.hibernate.annotations.FetchMode.SUBSELECT)
    private List<PurchaseOrderJpaEntity> orders = new ArrayList<>();

    protected CustomerJpaEntity() {
    }

    public CustomerJpaEntity(UUID id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.createdAt = Instant.now();
        this.preferences = new CustomerPreferences("en", false, List.of());
    }

    public UUID getId() {
        return id;
    }

    public long getVersion() {
        return version;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public List<PurchaseOrderJpaEntity> getOrders() {
        return orders;
    }

    public void setAddress(AddressEmbeddable address) {
        this.address = address;
    }

    public void setPreferences(CustomerPreferences preferences) {
        this.preferences = preferences;
    }
}
