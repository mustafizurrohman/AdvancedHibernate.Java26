package com.mustafizur.hibernateadvanced.api;

import com.mustafizur.hibernateadvanced.application.order.*;
import com.mustafizur.hibernateadvanced.application.validation.ValidationGroups;
import com.mustafizur.hibernateadvanced.infrastructure.service.OrderCommandService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
@Validated
public class OrderController {
    private final OrderCommandService commands;
    private final OrderRepository orders;

    public OrderController(OrderCommandService commands, OrderRepository orders) {
        this.commands = commands;
        this.orders = orders;
    }

    @PostMapping
    ResponseEntity<Void> create(@RequestBody @Validated(ValidationGroups.OrderedChecks.class) CreateOrderCommand command) {
        var id = commands.create(command);
        return ResponseEntity.created(URI.create("/api/v1/orders/" + id)).build();
    }

    @GetMapping("/{id}")
    ResponseEntity<OrderDetails> details(@PathVariable UUID id) {
        return orders.findDetails(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    List<OrderSummary> list(@RequestParam(defaultValue = "0") @Min(0) int page,
                            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size) {
        return orders.findSummaries(page, size);
    }
}
