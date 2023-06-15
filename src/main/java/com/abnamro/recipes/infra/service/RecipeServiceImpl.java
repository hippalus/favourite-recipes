package com.abnamro.recipes.infra.service;

import com.abnamro.recipes.domain.model.Recipe;
import com.abnamro.recipes.domain.service.RecipeService;
import com.abnamro.recipes.infra.data.entity.RecipeEntity;
import com.abnamro.recipes.infra.data.repository.RecipeJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService {

    private final RecipeJpaRepository recipeJpaRepository;

    @Override
    public Recipe createRecipe(final Recipe recipe) {
        final RecipeEntity recipeEntity = new RecipeEntity();

        this.setAttributes(recipeEntity, recipe);

        return this.recipeJpaRepository.save(recipeEntity).toModel();
    }

    @Override
    public Recipe retrieveRecipe(final UUID id) {
        return this.recipeJpaRepository.findById(id).orElseThrow().toModel();
    }

    @Override
    public Recipe updateRecipe(final UUID id, final Recipe recipe) {
        final RecipeEntity recipeEntity = this.recipeJpaRepository.findById(id).orElseThrow();

        this.setAttributes(recipeEntity, recipe);

        return this.recipeJpaRepository.save(recipeEntity).toModel();
    }

    @Override
    public void deleteRecipe(final UUID id) {
        this.recipeJpaRepository.deleteById(id);
    }

    private void setAttributes(final RecipeEntity recipeEntity, final Recipe recipe) {
        recipeEntity.setName(recipe.name());
        recipeEntity.setServings(recipe.servings());
        recipeEntity.setVegetarian(recipe.isVegetarian());
        recipeEntity.setIngredients(recipe.ingredients());
        recipeEntity.setInstructions(recipe.instructions());
    }
}
