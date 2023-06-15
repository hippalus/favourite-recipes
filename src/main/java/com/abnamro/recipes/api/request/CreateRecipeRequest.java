package com.abnamro.recipes.api.request;

import com.abnamro.recipes.domain.model.Recipe;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record CreateRecipeRequest(
        @NotNull
        @NotBlank
        String name,

        @NotNull
        Boolean isVegetarian,

        @Min(1)
        Integer servings,

        @Size(min = 1)
        Set<String> ingredients,

        @NotNull
        @NotBlank
        String instructions
) {

    public Recipe toModel() {
        return new Recipe(
                this.name,
                this.isVegetarian,
                this.servings,
                this.ingredients,
                this.instructions
        );
    }
}
