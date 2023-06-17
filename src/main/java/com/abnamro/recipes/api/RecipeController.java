package com.abnamro.recipes.api;

import com.abnamro.recipes.api.request.CreateRecipeRequest;
import com.abnamro.recipes.api.request.UpdateRecipeRequest;
import com.abnamro.recipes.api.response.RecipeResponse;
import com.abnamro.recipes.domain.model.Recipe;
import com.abnamro.recipes.domain.service.RecipeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/recipes")
@RequiredArgsConstructor
@Tag(name = "Recipes", description = "Create, read, update, and delete operations for recipes")
public class RecipeController {

    private final RecipeService recipeService;

    @PostMapping
    @Operation(
            summary = "Create a recipe",
            responses = {
                    @ApiResponse(
                            responseCode = "201", description = "Recipe Response",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = RecipeResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400", description = "Bad request",
                            content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class))
                    ),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error",
                            content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class))
                    )
            }
    )
    public ResponseEntity<RecipeResponse> createRecipe(@Valid @RequestBody final CreateRecipeRequest request) {

        final Recipe recipe = this.recipeService.createRecipe(request.toModel());

        final RecipeResponse recipeResponse = RecipeResponse.from(recipe);

        return ResponseEntity.created(URI.create("/recipes/%s".formatted(recipeResponse.id())))
                .body(recipeResponse);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Retrieve a recipe by ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "Recipe Response",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = RecipeResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400", description = "Bad request",
                            content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class))
                    ),
                    @ApiResponse(
                            responseCode = "404", description = "Not found",
                            content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class))
                    ),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error",
                            content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class))
                    )
            }
    )
    public ResponseEntity<RecipeResponse> retrieveRecipe(@PathVariable final UUID id) {
        final Recipe recipe = this.recipeService.retrieveRecipe(id);

        return ResponseEntity.ok(RecipeResponse.from(recipe));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update recipe",
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "Recipe Response",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = RecipeResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400", description = "Bad request",
                            content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class))
                    ),
                    @ApiResponse(
                            responseCode = "404", description = "Not found",
                            content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class))
                    ),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error",
                            content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class))
                    )
            }
    )
    public ResponseEntity<RecipeResponse> updateRecipe(
            @PathVariable final UUID id,
            @Valid @RequestBody final UpdateRecipeRequest request) {
        final Recipe recipe = this.recipeService.updateRecipe(id, request.toModel());

        return ResponseEntity.ok(RecipeResponse.from(recipe));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete recipe",
            responses = {
                    @ApiResponse(
                            responseCode = "202", description = "Accepted",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
                    ),
                    @ApiResponse(
                            responseCode = "400", description = "Bad request",
                            content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class))
                    ),
                    @ApiResponse(
                            responseCode = "404", description = "Not found",
                            content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class))
                    ),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error",
                            content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class))
                    )
            }
    )
    public ResponseEntity<Void> deleteRecipe(@PathVariable final UUID id) {
        this.recipeService.deleteRecipe(id);

        return ResponseEntity.accepted().build();
    }

}