package com.mustafizur.hibernateadvanced.infrastructure.persistence;

import jakarta.persistence.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "purchase_orders", indexes = {
        @Index(name = "ix_order_customer", columnList = "customer_id"),
        @Index(name = "ix_order_placed_at", columnList = "placed_at")
})
@Audited
@SoftDelete(columnName = "deleted")
@FilterDef(name = "minTotal", parameters = @ParamDef(name = "amount", type = BigDecimal.class))
@Filter(name = "minTotal", condition = "total_amount >= :amount")
@NamedEntityGraph(name = "Order.details", attributeNodes = {
        @NamedAttributeNode("customer"), @NamedAttributeNode(value = "lines", subgraph = "line-product")
}, subgraphs = @NamedSubgraph(name = "line-product", attributeNodes = @NamedAttributeNode("product")))
public class PurchaseOrderJpaEntity {
    @Id
    private UUID id;
    @Version
    private long version;
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id")
    private CustomerJpaEntity customer;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private com.mustafizur.hibernateadvanced.domain.order.OrderStatus status;
    @Column(name = "placed_at")
    private Instant placedAt;
    @Column(name = "total_amount", nullable = false, precision = 19, scale = 4)
    private BigDecimal totalAmount;
    @Column(nullable = false, length = 3)
    private String currency;
    @NotAudited
    @Formula("(select count(*) from order_lines l where l.order_id = id)")
    private int lineCount;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size = 32)
    private List<OrderLineJpaEntity> lines = new ArrayList<>();

    protected PurchaseOrderJpaEntity() {
    }

    public PurchaseOrderJpaEntity(UUID id, CustomerJpaEntity customer, com.mustafizur.hibernateadvanced.domain.order.OrderStatus status,
                                  Instant placedAt, BigDecimal totalAmount, String currency) {
        this.id = id;
        this.customer = customer;
        this.status = status;
        this.placedAt = placedAt;
        this.totalAmount = totalAmount;
        this.currency = currency;
    }

    public void addLine(OrderLineJpaEntity line) {
        lines.add(line);
    }

    public void removeLine(OrderLineJpaEntity line) {
        lines.remove(line);
    } // orphanRemoval=true -> DELETE

    public UUID getId() {
        return id;
    }

    public long getVersion() {
        return version;
    }

    public CustomerJpaEntity getCustomer() {
        return customer;
    }

    public com.mustafizur.hibernateadvanced.domain.order.OrderStatus getStatus() {
        return status;
    }

    public Instant getPlacedAt() {
        return placedAt;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public int getLineCount() {
        return lineCount;
    }

    public List<OrderLineJpaEntity> getLines() {
        return lines;
    }
}
