package com.abnamro.recipes.infra.service;

import com.abnamro.recipes.common.TestFixture;
import com.abnamro.recipes.domain.model.Recipe;
import com.abnamro.recipes.infra.data.entity.RecipeEntity;
import com.abnamro.recipes.infra.data.repository.RecipeJpaRepository;
import com.abnamro.recipes.infra.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import java.util.UUID;

import static com.abnamro.recipes.common.TestFixture.createRecipes;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class RecipeServiceImplTest {


    @Mock
    private RecipeJpaRepository recipeJpaRepository;

    private RecipeServiceImpl recipeService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        this.recipeService = new RecipeServiceImpl(this.recipeJpaRepository);
    }

    @Test
    void testCreateRecipe() {
        // Prepare the input
        final TestFixture.TestRecipes testRecipes = createRecipes();

        when(this.recipeJpaRepository.save(any())).thenReturn(testRecipes.recipeEntity());

        // Perform the method under test
        final Recipe createdRecipe = this.recipeService.createRecipe(testRecipes.recipe());

        // Verify the testRecipes
        assertThat(createdRecipe).isNotNull()
                .isEqualTo(testRecipes.recipeEntity().toModel());
        verify(this.recipeJpaRepository, times(1)).save(any());
    }

    @Test
    void testRetrieveRecipe() {
        final TestFixture.TestRecipes testRecipes = createRecipes();

        final RecipeEntity recipeEntity = testRecipes.recipeEntity();
        when(this.recipeJpaRepository.findById(any())).thenReturn(Optional.of(recipeEntity));

        // Perform the method under test
        final UUID recipeEntityId = recipeEntity.getId();
        final Recipe retrievedRecipe = this.recipeService.retrieveRecipe(recipeEntityId);

        // Verify the result
        assertThat(retrievedRecipe).isNotNull()
                .isEqualTo(recipeEntity.toModel());
        verify(this.recipeJpaRepository, times(1)).findById(recipeEntityId);
    }

    @Test
    void testRetrieveRecipe_NotFound() {
        final UUID recipeId = UUID.randomUUID();
        when(this.recipeJpaRepository.findById(recipeId)).thenReturn(Optional.empty());

        // Perform the method under test and verify the exception
        assertThatThrownBy(() -> this.recipeService.retrieveRecipe(recipeId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Recipe not found by id: " + recipeId);

        verify(this.recipeJpaRepository, times(1)).findById(recipeId);
    }

    @Test
    void testUpdateRecipe() {
        final TestFixture.TestRecipes existingRecipes = createRecipes();
        final RecipeEntity existingRecipeEntity = existingRecipes.recipeEntity();
        final UUID recipeEntityId = existingRecipeEntity.getId();

        when(this.recipeJpaRepository.findById(recipeEntityId)).thenReturn(Optional.of(existingRecipeEntity));

        final TestFixture.TestRecipes updatedRecipes = createRecipes();
        final RecipeEntity updatedEntity = updatedRecipes.recipeEntity();
        updatedEntity.setId(recipeEntityId);

        when(this.recipeJpaRepository.save(any())).thenReturn(updatedEntity);

        // Perform the method under test
        final Recipe updateRecipe = updatedEntity.toModel();
        final Recipe result = this.recipeService.updateRecipe(recipeEntityId, updateRecipe);

        // Verify the result
        assertThat(result).isNotNull().isEqualTo(updateRecipe);
        verify(this.recipeJpaRepository, times(1)).findById(recipeEntityId);
        verify(this.recipeJpaRepository, times(1)).save(any());
    }

    @Test
    void testUpdateRecipe_NotFound() {
        final UUID recipeId = UUID.randomUUID();
        when(this.recipeJpaRepository.findById(recipeId)).thenReturn(Optional.empty());
        final Recipe updateRecipe = createRecipes().recipe();

        // Perform the method under test and verify the exception
        assertThatThrownBy(() -> this.recipeService.updateRecipe(recipeId, updateRecipe))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Recipe not found by id: " + recipeId);
    }

    @Test
    void testDeleteRecipe_Exists() {
        final UUID recipeId = UUID.randomUUID();
        when(this.recipeJpaRepository.existsById(recipeId)).thenReturn(true);

        // Perform the method under test
        this.recipeService.deleteRecipe(recipeId);

        // Verify that the recipe is deleted
        verify(this.recipeJpaRepository, times(1)).existsById(recipeId);
        verify(this.recipeJpaRepository, times(1)).deleteById(recipeId);
    }

    @Test
    void testDeleteRecipe_NotFound() {
        final UUID recipeId = UUID.randomUUID();
        when(this.recipeJpaRepository.existsById(recipeId)).thenReturn(false);

        // Perform the method under test and verify the exception
        assertThatThrownBy(() -> {
            this.recipeService.deleteRecipe(recipeId);
        }).isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Recipe not found by id: " + recipeId);

        verify(this.recipeJpaRepository, times(1)).existsById(recipeId);
        verify(this.recipeJpaRepository, never()).deleteById(recipeId);
    }


}