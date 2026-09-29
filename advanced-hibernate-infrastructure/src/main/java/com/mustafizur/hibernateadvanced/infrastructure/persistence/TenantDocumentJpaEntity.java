package com.mustafizur.hibernateadvanced.infrastructure.persistence;

import jakarta.persistence.*;
import org.hibernate.annotations.TenantId;
import java.util.UUID;

@Entity
@Table(name = "tenant_documents")
public class TenantDocumentJpaEntity {
    @Id private UUID id;
    @TenantId @Column(name = "tenant_id", nullable = false) private String tenantId;
    @Column(nullable = false) private String title;
    protected TenantDocumentJpaEntity() { }
    public TenantDocumentJpaEntity(UUID id, String title) { this.id=id; this.title=title; }
    public UUID getId(){return id;} public String getTenantId(){return tenantId;} public String getTitle(){return title;}
}
