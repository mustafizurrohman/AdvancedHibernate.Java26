package com.mustafizur.hibernateadvanced.infrastructure.persistence;

import jakarta.persistence.*;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import java.math.BigDecimal;

@Entity
@Table(name = "order_lines")
@Audited
public class OrderLineJpaEntity {
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_line_seq")
    @SequenceGenerator(name = "order_line_seq", sequenceName = "order_line_seq", allocationSize = 50)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER, optional = false) @JoinColumn(name = "order_id") private PurchaseOrderJpaEntity order;
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "product_id") private ProductJpaEntity product;
    @Column(nullable = false) private String sku;
    @Column(nullable = false) private String productName;
    @Column(nullable = false) private int quantity;
    @Column(nullable = false, precision = 19, scale = 4) private BigDecimal unitPrice;
    @Column(nullable = false, length = 3) private String currency;
    protected OrderLineJpaEntity() { }
    public OrderLineJpaEntity(PurchaseOrderJpaEntity order, ProductJpaEntity product, String sku, String productName, int quantity, BigDecimal unitPrice, String currency){
        this.order=order; this.product=product; this.sku=sku; this.productName=productName; this.quantity=quantity; this.unitPrice=unitPrice; this.currency=currency;
    }
    public ProductJpaEntity getProduct(){return product;} public String getSku(){return sku;} public String getProductName(){return productName;}
    public int getQuantity(){return quantity;} public BigDecimal getUnitPrice(){return unitPrice;} public String getCurrency(){return currency;}
}
