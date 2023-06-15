package com.abnamro.recipes.api;

import com.abnamro.recipes.api.request.CreateRecipeRequest;
import com.abnamro.recipes.api.request.UpdateRecipeRequest;
import com.abnamro.recipes.api.response.RecipeResponse;
import com.abnamro.recipes.domain.model.Recipe;
import com.abnamro.recipes.domain.service.RecipeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/recipes")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;

    @PostMapping
    public ResponseEntity<RecipeResponse> createRecipe(@Valid @RequestBody final CreateRecipeRequest request) {

        final Recipe recipe = this.recipeService.createRecipe(request.toModel());

        final RecipeResponse recipeResponse = RecipeResponse.from(recipe);

        return new ResponseEntity<>(recipeResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecipeResponse> retrieveRecipe(@PathVariable final UUID id) {
        final Recipe recipe = this.recipeService.retrieveRecipe(id);

        return ResponseEntity.ok(RecipeResponse.from(recipe));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecipeResponse> updateRecipe(
            @PathVariable final UUID id,
            @Valid @RequestBody final UpdateRecipeRequest request) {
        final Recipe recipe = this.recipeService.updateRecipe(id, request.toModel());

        return ResponseEntity.ok(RecipeResponse.from(recipe));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable final UUID id) {
        this.recipeService.deleteRecipe(id);

        return ResponseEntity.accepted().build();
    }

}