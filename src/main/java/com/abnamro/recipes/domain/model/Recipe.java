package com.abnamro.recipes.domain.model;

import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;

public record Recipe(
        UUID id,
        String name,
        boolean isVegetarian,
        Integer servings,
        Set<String> ingredients,
        String instructions,
        ZonedDateTime createdDate,
        ZonedDateTime lastModifiedDate
) {
    public Recipe(final String name, final boolean isVegetarian, final Integer servings, final Set<String> ingredients, final String instructions) {
        this(null, name, isVegetarian, servings, ingredients, instructions, null, null);
    }
}
