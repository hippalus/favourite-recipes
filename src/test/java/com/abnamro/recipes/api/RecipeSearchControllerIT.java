package com.abnamro.recipes.api;

import com.abnamro.recipes.api.request.SearchResponse;
import com.abnamro.recipes.common.AbstractIT;
import com.abnamro.recipes.common.IT;
import com.abnamro.recipes.domain.model.Recipe;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static com.abnamro.recipes.common.TestFixture.getRecipes;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.ISOLATED;

@IT
@Sql(scripts = "classpath:sql/recipe.sql", executionPhase = BEFORE_TEST_METHOD, config = @SqlConfig(transactionMode = ISOLATED))
@Sql(scripts = "classpath:sql/cleanup.sql", executionPhase = AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = ISOLATED))
class RecipeSearchControllerIT extends AbstractIT {


    @Test
    void searchRecipes_ShouldReturnSearchResponse() {
        // Given
        final UriComponents uriComponents = UriComponentsBuilder.fromPath("/recipes/search")
                .queryParam("vegetarian", true)
                .queryParam("servings", 2)
                .queryParam("includedIngredients", Set.of())
                .queryParam("excludedIngredients", Set.of("cucumber"))
                .queryParam("searchText", "")
                .queryParam("page", 0)
                .queryParam("size", 10)
                .build();
        // When
        final ResponseEntity<SearchResponse> response = this.testRestTemplate.exchange(
                uriComponents.toUriString(),
                HttpMethod.GET,
                null,
                SearchResponse.class
        );

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().data()).isNotEmpty();
    }


    @Test
    void searchRecipes_WithMissingParameters_ShouldReturnDefaultResults() {
        // Given
        final UriComponents uriComponents = UriComponentsBuilder.fromPath("/recipes/search")
                .build();


        // Constructing the actual response with sample recipe data
        final List<Recipe> recipes = getRecipes();
        final SearchResponse expectedResponse = new SearchResponse(recipes);


        // When
        final ResponseEntity<SearchResponse> response = this.testRestTemplate.exchange(
                uriComponents.toUriString(),
                HttpMethod.GET,
                null,
                SearchResponse.class
        );

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
        assertThat(response.getBody()).isEqualTo(expectedResponse);
    }

    @Test
    void searchRecipes_WithInvalidParameters_ShouldReturnBadRequest() {
        // Given
        final UriComponents uriComponents = UriComponentsBuilder.fromPath("/recipes/search")
                .queryParam("vegetarian", "invalid")
                .queryParam("servings", "-1")
                .build();

        // When
        final ResponseEntity<ProblemDetail> response = this.testRestTemplate.getForEntity(
                uriComponents.toUriString(),
                ProblemDetail.class
        );

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void searchRecipes_WithPagination_ShouldReturnPaginatedResults() {
        // Given
        final UriComponents uriComponents = UriComponentsBuilder.fromPath("/recipes/search")
                .queryParam("page", 1)
                .queryParam("size", 2)
                .build();

        final List<Recipe> recipes = getRecipes();

        final SearchResponse expectedResponse = new SearchResponse(recipes.subList(2, 4));


        // When
        final ResponseEntity<SearchResponse> response = this.testRestTemplate.exchange(
                uriComponents.toUriString(),
                HttpMethod.GET,
                null,
                SearchResponse.class
        );

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
        assertThat(response.getBody()).isEqualTo(expectedResponse);
    }

    @Test
    void searchRecipes_WithEmptyResults_ShouldReturnEmptyResponse() {
        // Given
        final UriComponents uriComponents = UriComponentsBuilder.fromPath("/recipes/search")
                .queryParam("searchText", "nonexistent")
                .build();

        final List<Recipe> emptyList = Collections.emptyList();
        final SearchResponse expectedResponse = new SearchResponse(emptyList);

        // When
        final ResponseEntity<SearchResponse> response = this.testRestTemplate.exchange(
                uriComponents.toUriString(),
                HttpMethod.GET,
                null,
                SearchResponse.class
        );

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
        assertThat(response.getBody()).isEqualTo(expectedResponse);
    }

}