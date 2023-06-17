package com.abnamro.recipes.domain.model;

import lombok.Builder;

import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;

@Builder
public record Recipe(
        UUID id,
        String name,
        boolean vegetarian,
        Integer servings,
        Set<String> ingredients,
        String instructions,
        ZonedDateTime createdDate,
        ZonedDateTime lastModifiedDate
) {

}
