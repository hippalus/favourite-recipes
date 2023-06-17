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

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;

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

    private static List<Recipe> getRecipes() {
        return List.of(
                new Recipe(
                        UUID.fromString("183c75b3-6ec3-43cd-a5d8-3f12173df80d"),
                        "Recipe 7",
                        false,
                        3,
                        Set.of("Ingredient 7"),
                        "Instructions for Recipe 7",
                        ZonedDateTime.parse("2023-06-15T21:34:43.568467Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                        ZonedDateTime.parse("2023-06-15T21:34:43.568488Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME)
                ),
                new Recipe(
                        UUID.fromString("6ebf7c4d-7bb7-4a16-82c2-f4ac93222bbc"),
                        "Recipe 6",
                        true,
                        4,
                        Set.of("Ingredient 6"),
                        "Instructions for Recipe 6",
                        ZonedDateTime.parse("2023-06-15T15:33:06.468289Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                        ZonedDateTime.parse("2023-06-15T15:33:06.468427Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME)
                ),
                new Recipe(
                        UUID.fromString("a26bc586-bf65-4a21-8732-7bbadab63464"),
                        "Recipe 5",
                        true,
                        2,
                        Set.of("Ingredient 5"),
                        "Instructions for Recipe 5",
                        ZonedDateTime.parse("2023-06-15T15:31:19.799439Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                        ZonedDateTime.parse("2023-06-15T15:31:19.799459Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME)
                ),
                new Recipe(
                        UUID.fromString("30d0ffa3-6b33-4925-bef0-4868708e06ce"),
                        "Recipe 4",
                        false,
                        5,
                        Set.of("Ingredient 4"),
                        "Instructions for Recipe 4",
                        ZonedDateTime.parse("2023-06-15T13:30:31.291956Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                        ZonedDateTime.parse("2023-06-15T13:30:31.292043Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME)
                ),
                new Recipe(
                        UUID.fromString("12988cfc-7547-45a4-8ed6-7a6379eb078d"),
                        "Recipe 3",
                        true,
                        3,
                        Set.of("Ingredient 3"),
                        "Instructions for Recipe 3",
                        ZonedDateTime.parse("2023-06-15T13:30:27.343721Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                        ZonedDateTime.parse("2023-06-15T13:30:27.343750Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME)
                ),
                new Recipe(
                        UUID.fromString("6e4ab6fc-37de-4196-b992-902443745275"),
                        "Recipe 2",
                        false,
                        2,
                        Set.of("Ingredient 2"),
                        "Instructions for Recipe 2",
                        ZonedDateTime.parse("2023-06-15T13:27:59.001520Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                        ZonedDateTime.parse("2023-06-15T13:27:59.001560Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME)
                ), new Recipe(
                        UUID.fromString("28aa5a21-9940-4db2-adb1-41d2c6528af2"),
                        "Recipe 1",
                        true,
                        4,
                        Set.of("Ingredient 1"),
                        "Instructions for Recipe 1",
                        ZonedDateTime.parse("2023-06-15T13:12:42.791951Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                        ZonedDateTime.parse("2023-06-15T13:12:42.791971Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME)
                )
        );
    }
}