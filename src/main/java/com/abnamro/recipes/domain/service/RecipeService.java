package com.abnamro.recipes.domain.service;


import com.abnamro.recipes.domain.model.Recipe;

import java.util.UUID;

public interface RecipeService {

    Recipe createRecipe(Recipe recipe);

    Recipe retrieveRecipe(UUID id);

    Recipe updateRecipe(UUID id, Recipe recipe);

    void deleteRecipe(UUID id);
}
