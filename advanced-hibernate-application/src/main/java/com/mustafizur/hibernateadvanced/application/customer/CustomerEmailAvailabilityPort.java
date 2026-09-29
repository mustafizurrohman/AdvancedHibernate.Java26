package com.mustafizur.hibernateadvanced.application.customer;

public interface CustomerEmailAvailabilityPort {
    boolean isAvailable(String email);
}
