package com.abnamro.recipes.api.request;

import com.abnamro.recipes.domain.model.Recipe;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record UpdateRecipeRequest(
        @NotNull
        @NotBlank
        String name,
        @NotNull
        Boolean vegetarian,

        @Min(1)
        Integer servings,

        @Size(min = 1)
        Set<String> ingredients,

        @NotNull
        @NotBlank
        String instructions
) {

    public Recipe toModel() {
        return Recipe.builder()
                .name(this.name)
                .vegetarian(this.vegetarian)
                .ingredients(this.ingredients)
                .servings(this.servings)
                .instructions(this.instructions)
                .build();
    }
}
