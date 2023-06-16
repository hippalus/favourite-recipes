package com.abnamro.recipes.api;

import com.abnamro.recipes.api.request.SearchResponse;
import com.abnamro.recipes.common.AbstractIT;
import com.abnamro.recipes.common.IT;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Set;

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
}