package com.mustafizur.hibernateadvanced.infrastructure.persistence;

import java.util.List;

public record CustomerPreferences(String locale, boolean marketingEmails, List<String> favoriteCategories) {
    public CustomerPreferences {
        favoriteCategories = favoriteCategories == null ? List.of() : List.copyOf(favoriteCategories);
    }
}
