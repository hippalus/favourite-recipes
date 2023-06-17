package com.abnamro.recipes.infra.data.repository;

import com.abnamro.recipes.common.IT;
import com.abnamro.recipes.common.TestFixture;
import com.abnamro.recipes.domain.model.RecipeSearchCriteria;
import com.abnamro.recipes.infra.data.entity.RecipeEntity;
import com.abnamro.recipes.infra.data.specification.RecipeSpecification;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;

@IT
@Transactional
class RecipeJpaRepositoryIT {

    @Autowired
    private RecipeJpaRepository recipeRepository;

    @BeforeEach
    void setup() {
        final List<RecipeEntity> recipeEntities = TestFixture.getRecipes()
                .stream()
                .map(recipe -> {
                    final RecipeEntity recipeEntity = new RecipeEntity();
                    recipeEntity.setId(recipe.id());
                    recipeEntity.setName(recipe.name());
                    recipeEntity.setServings(recipe.servings());
                    recipeEntity.setVegetarian(recipe.vegetarian());
                    recipeEntity.setIngredients(recipe.ingredients());
                    recipeEntity.setInstructions(recipe.instructions());
                    return recipeEntity;
                }).toList();

        this.recipeRepository.saveAll(recipeEntities);
    }

    @AfterEach
    void tearDown() {
        this.recipeRepository.deleteAll();
    }

    @Test
    void findAll_WithVegetarianCondition_ShouldReturnVegetarianRecipes() {
        // Given
        final RecipeSearchCriteria searchCriteria = RecipeSearchCriteria.builder()
                .vegetarian(true)
                .build();
        final Specification<RecipeEntity> specification = new RecipeSpecification(searchCriteria);

        // When
        final List<RecipeEntity> recipes = this.recipeRepository.findAll(specification);

        // Then
        // Assert that all recipes in the result are vegetarian
        assertThat(recipes).isNotEmpty().allMatch(RecipeEntity::isVegetarian);
    }

    @Test
    void findAll_WithServingsCondition_ShouldReturnRecipesWithSpecificServings() {
        // Given
        final RecipeSearchCriteria searchCriteria = RecipeSearchCriteria.builder()
                .servings(4)
                .build();
        final Specification<RecipeEntity> specification = new RecipeSpecification(searchCriteria);

        // When
        final List<RecipeEntity> recipes = this.recipeRepository.findAll(specification);

        // Then
        // Assert that all recipes in the result have the specified number of servings
        assertThat(recipes).isNotEmpty().allMatch(recipe -> recipe.getServings() == 4);
    }

    @Test
    void findAll_WithIncludedIngredientsCondition_ShouldReturnRecipesWithIncludedIngredients() {
        // Given
        final RecipeSearchCriteria searchCriteria = RecipeSearchCriteria.builder()
                .includedIngredients(Set.of("Ingredient 1", "Ingredient 2"))
                .build();

        final Specification<RecipeEntity> specification = new RecipeSpecification(searchCriteria);

        // When
        final List<RecipeEntity> recipes = this.recipeRepository.findAll(specification);

        // Then
        // Assert that all recipes in the result contain at least one of the included ingredients
        assertThat(recipes).isNotEmpty().allMatch(recipe -> recipe.getIngredients().stream()
                .anyMatch(ingredient -> searchCriteria.includedIngredients().contains(ingredient)));
    }

    @Test
    void findAll_WithExcludedIngredientsCondition_ShouldReturnRecipesWithoutExcludedIngredients() {
        // Given
        final RecipeSearchCriteria searchCriteria = RecipeSearchCriteria.builder()
                .excludedIngredients(Set.of("Ingredient 3", "Ingredient 4"))
                .build();

        final Specification<RecipeEntity> specification = new RecipeSpecification(searchCriteria);

        // When
        final List<RecipeEntity> recipes = this.recipeRepository.findAll(specification);

        // Then
        // Assert that no recipes in the result contain any of the excluded ingredients
        assertThat(recipes).isNotEmpty()
                .noneMatch(recipe -> recipe.getIngredients().stream()
                        .anyMatch(ingredient -> searchCriteria.excludedIngredients().contains(ingredient))
                );
    }

    @Test
    void findAll_WithSearchTextCondition_ShouldReturnRecipesWithMatchingInstructions() {
        // Given
        final RecipeSearchCriteria searchCriteria = RecipeSearchCriteria.builder()
                .searchText("Instructions for Recipe")
                .build();

        final Specification<RecipeEntity> specification = new RecipeSpecification(searchCriteria);

        // When
        final List<RecipeEntity> recipes = this.recipeRepository.findAll(specification);

        // Then
        // Assert that all recipes in the result have instructions containing the search text
        assertThat(recipes).isNotEmpty().allMatch(recipe -> recipe.getInstructions().toLowerCase()
                .contains(searchCriteria.searchText().toLowerCase()));
    }

    @Test
    void testDeleteById() {
        // Create a sample recipe entity
        final RecipeEntity recipeEntity = TestFixture.createRecipes().recipeEntity();

        // Save the recipe entity
        this.recipeRepository.save(recipeEntity);

        // Delete the recipe entity by its id
        this.recipeRepository.deleteById(recipeEntity.getId());

        // Verify that the recipe entity has been deleted
        final Optional<RecipeEntity> deletedRecipe = this.recipeRepository.findById(recipeEntity.getId());
        assertFalse(deletedRecipe.isPresent());
    }

}