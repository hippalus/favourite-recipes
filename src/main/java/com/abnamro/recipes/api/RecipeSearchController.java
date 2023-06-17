package com.abnamro.recipes.api;

import com.abnamro.recipes.api.docs.CustomPageable;
import com.abnamro.recipes.api.request.SearchResponse;
import com.abnamro.recipes.domain.model.Recipe;
import com.abnamro.recipes.domain.model.RecipeSearchCriteria;
import com.abnamro.recipes.domain.service.RecipeSearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recipes/search")
@Tag(name = "Search Recipes", description = "Search operations for recipes")
public class RecipeSearchController {

    private final RecipeSearchService recipeSearchService;

    @GetMapping
    @Operation(
            summary = "Search recipes",
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "Search Response",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = SearchResponse.class))
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
    @CustomPageable
    public SearchResponse searchRecipes(@RequestParam(required = false) final Boolean vegetarian,
                                        @RequestParam(required = false) final Integer servings,
                                        @RequestParam(required = false) final Set<String> includedIngredients,
                                        @RequestParam(required = false) final Set<String> excludedIngredients,
                                        @RequestParam(required = false) final String searchText,
                                        @PageableDefault(size = 25, sort = "lastModifiedDate", direction = Sort.Direction.DESC) final Pageable pageable
    ) {
        final RecipeSearchCriteria searchCriteria = RecipeSearchCriteria.builder()
                .vegetarian(vegetarian)
                .servings(servings)
                .includedIngredients(includedIngredients)
                .excludedIngredients(excludedIngredients)
                .searchText(searchText)
                .pageable(pageable)
                .build();

        final List<Recipe> searchedRecipes = this.recipeSearchService.searchRecipes(searchCriteria);

        return SearchResponse.from(searchedRecipes);
    }

}
