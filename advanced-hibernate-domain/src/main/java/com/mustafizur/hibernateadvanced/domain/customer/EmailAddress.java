package com.mustafizur.hibernateadvanced.domain.customer;

import java.util.Locale;
import java.util.regex.Pattern;

public record EmailAddress(String value) {
    private static final Pattern SIMPLE = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
    public EmailAddress {
        if (value == null || !SIMPLE.matcher(value.strip()).matches()) throw new IllegalArgumentException("Invalid email");
        value = value.strip().toLowerCase(Locale.ROOT);
    }
}
