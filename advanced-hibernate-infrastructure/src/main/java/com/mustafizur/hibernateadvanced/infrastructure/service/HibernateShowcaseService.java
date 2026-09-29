package com.mustafizur.hibernateadvanced.infrastructure.service;

import com.mustafizur.hibernateadvanced.infrastructure.persistence.*;
import com.mustafizur.hibernateadvanced.infrastructure.persistence.payment.CardPaymentJpaEntity;
import jakarta.persistence.*;
import jakarta.persistence.criteria.Predicate;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

@Service
public class HibernateShowcaseService {
    @PersistenceContext
    private EntityManager em;

    /**
     * JOIN FETCH: explicit fetch plan, avoiding N+1 for to-one association.
     */
    @Transactional(readOnly = true)
    public List<PurchaseOrderJpaEntity> fetchJoinOrders() {
        return em.createQuery("""
                select o from PurchaseOrderJpaEntity o
                join fetch o.customer
                order by o.placedAt desc
                """, PurchaseOrderJpaEntity.class).setMaxResults(50).getResultList();
    }

    /**
     * Runtime EntityGraph: query shape can vary without changing mapping annotations.
     */
    @Transactional(readOnly = true)
    public List<PurchaseOrderJpaEntity> dynamicEntityGraph() {
        var graph = em.createEntityGraph(PurchaseOrderJpaEntity.class);
        graph.addAttributeNodes("customer", "lines");
        return em.createQuery("select o from PurchaseOrderJpaEntity o order by o.placedAt desc", PurchaseOrderJpaEntity.class)
                .setHint("jakarta.persistence.fetchgraph", graph).setMaxResults(25).getResultList();
    }

    /**
     * Natural-id lookup bypasses application-level SKU -> id lookup.
     */
    @Transactional(readOnly = true)
    public ProductJpaEntity byNaturalId(String sku) {
        return em.unwrap(Session.class).byNaturalId(ProductJpaEntity.class).using("sku", sku).load();
    }

    /**
     * Hibernate multi-load batches identifiers efficiently and consults caches when configured.
     */
    @Transactional(readOnly = true)
    public List<ProductJpaEntity> multiLoad(List<UUID> ids) {
        return em.unwrap(Session.class).byMultipleIds(ProductJpaEntity.class).multiLoad(ids);
    }

    /**
     * Optimistic locking is automatic through @Version; this method illustrates managed dirty checking.
     */
    @Transactional
    public void reserveOptimistically(UUID productId, int quantity) {
        var product = em.find(ProductJpaEntity.class, productId);
        product.reserve(quantity);
    }

    /**
     * PESSIMISTIC_WRITE maps to SELECT ... FOR UPDATE where supported.
     */
    @Transactional
    public void reservePessimistically(UUID productId, int quantity) {
        var product = em.find(ProductJpaEntity.class, productId, LockModeType.PESSIMISTIC_WRITE);
        product.reserve(quantity);
    }

    /**
     * Bulk DML executes in the database and bypasses managed entity state.
     */
    @Transactional
    public int bulkCancelOldDrafts(Instant cutoff) {
        int changed = em.unwrap(Session.class).createMutationQuery("""
                update PurchaseOrderJpaEntity o
                set o.status = com.mustafizur.hibernateadvanced.domain.order.OrderStatus.CANCELLED
                where o.status = com.mustafizur.hibernateadvanced.domain.order.OrderStatus.DRAFT
                  and o.placedAt < :cutoff
                """).setParameter("cutoff", cutoff).executeUpdate();
        em.clear();
        return changed;
    }

    /**
     * Dynamic Hibernate filter. Always disable in finally to avoid leaking state in the Session.
     */
    @Transactional(readOnly = true)
    public List<PurchaseOrderJpaEntity> filteredByMinimumTotal(BigDecimal amount) {
        Session session = em.unwrap(Session.class);
        Filter filter = session.enableFilter("minTotal").setParameter("amount", amount);
        try {
            return em.createQuery("select o from PurchaseOrderJpaEntity o order by o.totalAmount desc", PurchaseOrderJpaEntity.class).getResultList();
        } finally {
            session.disableFilter(filter.getName());
        }
    }

    /**
     * Criteria API: appropriate when predicate structure is truly dynamic.
     */
    @Transactional(readOnly = true)
    public List<ProductJpaEntity> criteriaProductSearch(String name, BigDecimal minPrice, BigDecimal maxPrice) {
        var cb = em.getCriteriaBuilder();
        var cq = cb.createQuery(ProductJpaEntity.class);
        var p = cq.from(ProductJpaEntity.class);
        var predicates = new ArrayList<Predicate>();
        if (name != null && !name.isBlank())
            predicates.add(cb.like(cb.lower(p.get("name")), "%" + name.toLowerCase(Locale.ROOT) + "%"));
        if (minPrice != null) predicates.add(cb.greaterThanOrEqualTo(p.get("price"), minPrice));
        if (maxPrice != null) predicates.add(cb.lessThanOrEqualTo(p.get("price"), maxPrice));
        cq.where(predicates.toArray(Predicate[]::new)).orderBy(cb.asc(p.get("name")));
        return em.createQuery(cq).setMaxResults(100).getResultList();
    }

    /**
     * Keyset/seek pagination: stable and efficient for deep scrolling.
     */
    @Transactional(readOnly = true)
    public List<PurchaseOrderJpaEntity> keysetPage(Instant beforePlacedAt, UUID beforeId, int size) {
        return em.createQuery("""
                        select o from PurchaseOrderJpaEntity o
                        where (:at is null)
                           or o.placedAt < :at
                           or (o.placedAt = :at and o.id < :id)
                        order by o.placedAt desc, o.id desc
                        """, PurchaseOrderJpaEntity.class)
                .setParameter("at", beforePlacedAt)
                .setParameter("id", beforeId == null ? new UUID(Long.MAX_VALUE, Long.MAX_VALUE) : beforeId)
                .setMaxResults(size).getResultList();
    }

    /**
     * CTE in HQL.
     */
    @Transactional(readOnly = true)
    public List<PurchaseOrderJpaEntity> expensiveOrders(BigDecimal threshold) {
        return em.createQuery("""
                with ExpensiveOrders as (
                    select o.id as id from PurchaseOrderJpaEntity o where o.totalAmount >= :threshold
                )
                select o from PurchaseOrderJpaEntity o
                where o.id in (select e.id from ExpensiveOrders e)
                order by o.totalAmount desc
                """, PurchaseOrderJpaEntity.class).setParameter("threshold", threshold).getResultList();
    }

    /**
     * Polymorphic HQL + treat().
     */
    @Transactional(readOnly = true)
    public List<CardPaymentJpaEntity> cardPayments() {
        return em.createQuery("""
                select treat(p as CardPaymentJpaEntity)
                from PaymentJpaEntity p
                where type(p) = CardPaymentJpaEntity
                """, CardPaymentJpaEntity.class).getResultList();
    }


    /**
     * First-level cache / identity map: repeated find in one persistence context returns the same managed instance.
     */
    @Transactional(readOnly = true)
    public boolean firstLevelCacheIdentity(UUID id) {
        var first = em.find(ProductJpaEntity.class, id);
        var second = em.find(ProductJpaEntity.class, id);
        return first == second;
    }

    /**
     * Query cache example. It becomes effective only when the cache Spring profile enables query caching.
     */
    @Transactional(readOnly = true)
    public List<CategoryJpaEntity> cacheableCategories() {
        return em.createQuery("select c from CategoryJpaEntity c order by c.name", CategoryJpaEntity.class)
                .setHint(org.hibernate.jpa.HibernateHints.HINT_CACHEABLE, true)
                .getResultList();
    }

    /**
     * FlushMode.COMMIT defers automatic query-time synchronization when the use case permits it.
     */
    @Transactional
    public long countProductsWithoutQueryTimeAutoFlush() {
        return em.createQuery("select count(p) from ProductJpaEntity p", Long.class)
                .setFlushMode(FlushModeType.COMMIT)
                .getSingleResult();
    }

    /**
     * HQL set operation.
     */
    @Transactional(readOnly = true)
    public List<String> customerEmailUnion(String firstPattern, String secondPattern) {
        return em.createQuery("""
                        select c.email from CustomerJpaEntity c where c.email like :a
                        union
                        select c.email from CustomerJpaEntity c where c.email like :b
                        """, String.class)
                .setParameter("a", firstPattern)
                .setParameter("b", secondPattern)
                .getResultList();
    }

    /**
     * LATERAL join / top-N-per-parent HQL example.
     */
    @Transactional(readOnly = true)
    public List<Object[]> mostExpensiveLinePerOrder() {
        return em.createQuery("""
                select o.id, topLine.productName, topLine.unitPrice
                from PurchaseOrderJpaEntity o
                left join lateral (
                    select l.productName as productName, l.unitPrice as unitPrice
                    from o.lines l
                    order by l.unitPrice desc
                    limit 1
                ) topLine
                order by o.placedAt desc
                """, Object[].class).getResultList();
    }

    /**
     * Native SQL remains valid for database-specific reporting.
     */
    @Transactional(readOnly = true)
    public List<Object[]> nativeMonthlyRevenue() {
        return em.createNativeQuery("""
                select extract(year from placed_at) as year,
                       extract(month from placed_at) as month,
                       sum(total_amount) as revenue
                  from purchase_orders
                 where deleted = false
                 group by extract(year from placed_at), extract(month from placed_at)
                 order by year, month
                """).getResultList();
    }

    /**
     * StatelessSession: no persistence context/dirty checking; useful for ETL-style work.
     */
    @Transactional
    public void statelessInsertCategories(List<CategoryJpaEntity> categories) {
        var sessionFactory = em.getEntityManagerFactory().unwrap(org.hibernate.SessionFactory.class);
        try (StatelessSession stateless = sessionFactory.openStatelessSession()) {
            var tx = stateless.beginTransaction();
            try {
                categories.forEach(stateless::insert);
                tx.commit();
            } catch (RuntimeException ex) {
                tx.rollback();
                throw ex;
            }
        }
    }

    /**
     * Programmatic flush/clear bounds persistence-context memory during large writes.
     */
    @Transactional
    public void batchPersistProducts(List<ProductJpaEntity> products) {
        int i = 0;
        for (var product : products) {
            em.persist(product);
            if (++i % 50 == 0) {
                em.flush();
                em.clear();
            }
        }
    }
}
