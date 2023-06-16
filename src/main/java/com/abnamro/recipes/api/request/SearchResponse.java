package com.abnamro.recipes.api.request;

import com.abnamro.recipes.domain.model.Recipe;

import java.util.List;

public record SearchResponse(List<Recipe> data) {
}
