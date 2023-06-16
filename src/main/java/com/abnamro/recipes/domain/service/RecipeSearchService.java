package com.abnamro.recipes.domain.service;

import com.abnamro.recipes.domain.model.Recipe;
import com.abnamro.recipes.domain.model.RecipeSearchCriteria;

import java.util.List;

public interface RecipeSearchService {


    List<Recipe> searchRecipes(RecipeSearchCriteria recipeSearchCriteria);
}
