package com.abnamro.recipes.api.response;

import com.abnamro.recipes.domain.model.Recipe;

import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;

public record RecipeResponse(
        UUID id,
        String name,
        Boolean vegetarian,
        Integer servings,
        Set<String> ingredients,
        String instructions,
        ZonedDateTime createdDate,
        ZonedDateTime lastModifiedDate
) {

    public static RecipeResponse from(final Recipe recipe) {
        return new RecipeResponse(
                recipe.id(),
                recipe.name(),
                recipe.vegetarian(),
                recipe.servings(),
                recipe.ingredients(),
                recipe.instructions(),
                recipe.createdDate(),
                recipe.lastModifiedDate()
        );
    }
}
