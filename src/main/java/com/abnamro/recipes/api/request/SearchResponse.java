package com.abnamro.recipes.api.request;

import com.abnamro.recipes.api.response.RecipeResponse;
import com.abnamro.recipes.domain.model.Recipe;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

public record SearchResponse(List<RecipeResponse> recipes) {

    public static SearchResponse from(final List<Recipe> searchedRecipes) {
        if (CollectionUtils.isEmpty(searchedRecipes)) {
            return new SearchResponse(List.of());
        }
        return new SearchResponse(searchedRecipes.stream().map(RecipeResponse::from).toList());
    }
}
