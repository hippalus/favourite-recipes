package com.abnamro.recipes.api.request;

import com.abnamro.recipes.domain.model.Recipe;

import java.util.Set;

public record CreateRecipeRequest(
        String name,
        boolean isVegetarian,
        Integer servings,
        Set<String> ingredients,
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
