package com.mustafizur.hibernateadvanced.api;

import com.mustafizur.hibernateadvanced.application.report.AnalyticsPort;
import com.mustafizur.hibernateadvanced.infrastructure.service.HibernateShowcaseService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/showcase")
public class ShowcaseController {
    private final HibernateShowcaseService showcase;
    private final AnalyticsPort analytics;

    public ShowcaseController(HibernateShowcaseService showcase, AnalyticsPort analytics) {
        this.showcase = showcase;
        this.analytics = analytics;
    }

    @GetMapping("/natural-id/{sku}")
    Object naturalId(@PathVariable String sku) {
        var p = showcase.byNaturalId(sku);
        return java.util.Map.of("id", p.getId(), "sku", p.getSku(), "name", p.getName());
    }

    @GetMapping("/analytics/running-revenue")
    List<AnalyticsPort.RunningRevenue> runningRevenue(@RequestParam Instant from) {
        return analytics.runningRevenue(from);
    }

    @GetMapping("/analytics/top-customers")
    List<AnalyticsPort.CustomerRank> topCustomers(@RequestParam(defaultValue = "10") int limit) {
        return analytics.topCustomers(limit);
    }

    @GetMapping("/criteria/products")
    Object criteria(@RequestParam(required = false) String name, @RequestParam(required = false) BigDecimal minPrice, @RequestParam(required = false) BigDecimal maxPrice) {
        return showcase.criteriaProductSearch(name, minPrice, maxPrice).stream().map(p -> java.util.Map.of("id", p.getId(), "sku", p.getSku(), "name", p.getName(), "price", p.getPrice())).toList();
    }

    @GetMapping("/cte/expensive-orders")
    Object cte(@RequestParam BigDecimal threshold) {
        return showcase.expensiveOrders(threshold).stream().map(o -> java.util.Map.of("id", o.getId(), "total", o.getTotalAmount(), "currency", o.getCurrency())).toList();
    }

    @PostMapping("/inventory/{productId}/optimistic")
    void optimistic(@PathVariable UUID productId, @RequestParam int quantity) {
        showcase.reserveOptimistically(productId, quantity);
    }

    @PostMapping("/inventory/{productId}/pessimistic")
    void pessimistic(@PathVariable UUID productId, @RequestParam int quantity) {
        showcase.reservePessimistically(productId, quantity);
    }
}
