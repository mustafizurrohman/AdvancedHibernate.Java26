package com.mustafizur.hibernateadvanced.infrastructure.service;

import com.mustafizur.hibernateadvanced.application.order.CreateOrderCommand;
import com.mustafizur.hibernateadvanced.application.order.OrderRepository;
import com.mustafizur.hibernateadvanced.domain.common.Money;
import com.mustafizur.hibernateadvanced.domain.customer.CustomerId;
import com.mustafizur.hibernateadvanced.domain.order.OrderId;
import com.mustafizur.hibernateadvanced.domain.order.PurchaseOrder;
import com.mustafizur.hibernateadvanced.domain.product.ProductId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderCommandService {
    private final OrderRepository orders;
    public OrderCommandService(OrderRepository orders) { this.orders = orders; }

    @Transactional
    public java.util.UUID create(CreateOrderCommand command) {
        var order = new PurchaseOrder(OrderId.newId(), new CustomerId(command.customerId()));
        for (var line : command.lines()) {
            order.addLine(new ProductId(line.productId()), line.sku(), line.productName(), line.quantity(), new Money(line.unitPrice(), line.currency()));
        }
        order.place();
        orders.save(order);
        return order.id().value();
    }
}
