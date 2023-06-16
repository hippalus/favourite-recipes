package com.abnamro.recipes.domain.model;

import lombok.Builder;
import org.springframework.data.domain.Pageable;

import java.util.Set;

@Builder
public record RecipeSearchCriteria(
        Boolean vegetarian,
        Integer servings,
        Set<String> includedIngredients,
        Set<String> excludedIngredients,
        String searchText,
        Pageable pageable
) {

    public boolean isVegetarian() {
        return Boolean.TRUE.equals(this.vegetarian);
    }

}