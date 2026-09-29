package com.mustafizur.hibernateadvanced.application.report;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public interface AnalyticsPort {
    List<RunningRevenue> runningRevenue(Instant from);

    List<CustomerRank> topCustomers(int limit);

    record RunningRevenue(Instant at, BigDecimal orderTotal, BigDecimal runningTotal) {
    }

    record CustomerRank(String customerName, BigDecimal total, long rank) {
    }
}
