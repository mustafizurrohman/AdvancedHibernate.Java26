package com.mustafizur.hibernateadvanced.infrastructure.service;

import com.mustafizur.hibernateadvanced.infrastructure.persistence.PurchaseOrderJpaEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.envers.AuditReaderFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class EnversHistoryService {
    @PersistenceContext
    private EntityManager em;

    @Transactional(readOnly = true)
    public List<Number> revisions(UUID orderId) {
        return AuditReaderFactory.get(em).getRevisions(PurchaseOrderJpaEntity.class, orderId);
    }

    @Transactional(readOnly = true)
    public PurchaseOrderJpaEntity atRevision(UUID orderId, Number revision) {
        return AuditReaderFactory.get(em).find(PurchaseOrderJpaEntity.class, orderId, revision);
    }
}
