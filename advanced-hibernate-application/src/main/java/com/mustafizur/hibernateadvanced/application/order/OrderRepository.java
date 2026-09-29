package com.mustafizur.hibernateadvanced.application.order;

import com.mustafizur.hibernateadvanced.domain.order.PurchaseOrder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {
    void save(PurchaseOrder order);

    Optional<OrderDetails> findDetails(UUID orderId);

    List<OrderSummary> findSummaries(int page, int size);
}
