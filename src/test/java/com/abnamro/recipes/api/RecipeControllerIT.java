package com.abnamro.recipes.api;

import com.abnamro.recipes.api.request.CreateRecipeRequest;
import com.abnamro.recipes.api.request.UpdateRecipeRequest;
import com.abnamro.recipes.api.response.RecipeResponse;
import com.abnamro.recipes.common.AbstractIT;
import com.abnamro.recipes.common.IT;
import com.abnamro.recipes.domain.model.Recipe;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.ISOLATED;

@IT
@Sql(scripts = "classpath:sql/recipe.sql", executionPhase = BEFORE_TEST_METHOD, config = @SqlConfig(transactionMode = ISOLATED))
@Sql(scripts = "classpath:sql/cleanup.sql", executionPhase = AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = ISOLATED))
class RecipeControllerIT extends AbstractIT {

    private static final Faker faker = new Faker();

    @Test
    void testCreateRecipe() {
        // Prepare the request body
        final CreateRecipeRequest request = new CreateRecipeRequest(
                faker.food().dish(),
                faker.bool().bool(),
                faker.number().randomDigit(),
                IntStream.rangeClosed(0, 10).mapToObj(i -> faker.food().ingredient()).collect(Collectors.toSet()),
                "Test Instruction"
        );

        // Set up the mock behavior for recipeService.createRecipe
        final Recipe mockRecipe = new Recipe(
                request.name(),
                request.isVegetarian(),
                request.servings(),
                request.ingredients(),
                request.instructions()
        );

        // Perform the POST request
        final ResponseEntity<RecipeResponse> response = this.testRestTemplate.postForEntity("/recipes", request, RecipeResponse.class);

        // Verify the response status code and body
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        final RecipeResponse responseBody = response.getBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.id()).isNotNull();
        assertThat(responseBody)
                .returns(mockRecipe.name(), RecipeResponse::name)
                .returns(mockRecipe.servings(), RecipeResponse::servings)
                .returns(mockRecipe.ingredients(), RecipeResponse::ingredients)
                .returns(mockRecipe.instructions(), RecipeResponse::instructions)
                .returns(mockRecipe.isVegetarian(), RecipeResponse::isVegetarian);

        assertThat(responseBody.createdDate()).isNotNull();
        assertThat(responseBody.lastModifiedDate()).isNotNull();
    }

    @Test
    void testCreateRecipe_InvalidRequest_BlankName() {
        // Prepare the request body with blank name
        final CreateRecipeRequest request = new CreateRecipeRequest("", true, 4, Set.of("Ingredient 1", "Ingredient 2"), "Instructions");

        // Perform the POST request and verify the exception
        final ResponseEntity<ProblemDetail> responseEntity = this.testRestTemplate.postForEntity("/recipes", request, ProblemDetail.class);


        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody().getDetail()).isEqualTo("Wrong fields : name must not be blank");
    }

    @Test
    void testCreateRecipe_InvalidRequest_NullInstructions() {
        // Prepare the request body with null instructions
        final CreateRecipeRequest request = new CreateRecipeRequest("Recipe 1", true, 4, Set.of("Ingredient 1", "Ingredient 2"), null);

        // Perform the POST request and verify the exception
        final ResponseEntity<ProblemDetail> responseEntity = this.testRestTemplate.postForEntity("/recipes", request, ProblemDetail.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody().getDetail()).contains("Wrong fields", "instructions must not be blank", "instructions must not be null");
    }

    @Test
    void testCreateRecipe_InvalidRequest_InvalidServings() {
        // Prepare the request body with null instructions
        final CreateRecipeRequest request = new CreateRecipeRequest("Recipe 1", false, 0, Set.of("Ingredient 1", "Ingredient 2"), null);

        // Perform the POST request and verify the exception
        final ResponseEntity<ProblemDetail> responseEntity = this.testRestTemplate.postForEntity("/recipes", request, ProblemDetail.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody().getDetail()).contains("Wrong fields", "instructions must not be blank", "instructions must not be null");
    }

    @Test
    void testRetrieveRecipe() {
        final UUID recipeId = UUID.fromString("6ebf7c4d-7bb7-4a16-82c2-f4ac93222bbc");

        // Perform the GET request
        final ResponseEntity<RecipeResponse> response = this.testRestTemplate.getForEntity("/recipes/{id}", RecipeResponse.class, recipeId);

        // Verify the response status code and body
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        final RecipeResponse responseBody = response.getBody();
        assertThat(responseBody).isNotNull()
                .returns(recipeId, RecipeResponse::id)
                .returns("string", RecipeResponse::name)
                .returns(1, RecipeResponse::servings)
                .returns(true, RecipeResponse::isVegetarian)
                .returns(Set.of("string"), RecipeResponse::ingredients)
                .returns("string", RecipeResponse::instructions)
                .returns(ZonedDateTime.parse("2023-06-15T15:33:06.468289Z"), RecipeResponse::createdDate)
                .returns(ZonedDateTime.parse("2023-06-15T15:33:06.468427Z"), RecipeResponse::lastModifiedDate);
    }

    @Test
    void testRetrieveRecipe_NotFound() {
        final UUID recipeId = UUID.randomUUID();

        // Perform the GET request
        final ResponseEntity<ProblemDetail> response = this.testRestTemplate.getForEntity("/recipes/{id}", ProblemDetail.class, recipeId);

        // Verify the response status code and body
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().getDetail()).isEqualTo("Recipe not found by id: " + recipeId);
    }

    @Test
    void testUpdateRecipe() {
        final UUID recipeId = UUID.fromString("28aa5a21-9940-4db2-adb1-41d2c6528af2");
        final UpdateRecipeRequest request = new UpdateRecipeRequest(
                faker.food().dish(),
                faker.bool().bool(),
                faker.number().randomDigit(),
                IntStream.rangeClosed(0, 10).mapToObj(i -> faker.food().ingredient()).collect(Collectors.toSet()),
                "Test Instraction"
        );

        final ZonedDateTime dateTime = ZonedDateTime.now(ZoneOffset.UTC);

        final RecipeResponse expected = new RecipeResponse(
                recipeId,
                request.name(),
                request.isVegetarian(),
                request.servings(),
                request.ingredients(),
                request.instructions(),
                ZonedDateTime.parse("2023-06-15T13:12:42.791951Z"),
                dateTime
        );

        // Perform the PUT request
        final ResponseEntity<RecipeResponse> response = this.testRestTemplate.exchange("/recipes/{id}", HttpMethod.PUT, new HttpEntity<>(request), RecipeResponse.class, recipeId);

        // Verify the response status code and body
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        final RecipeResponse responseBody = response.getBody();
        assertThat(responseBody).isNotNull()
                .returns(recipeId, RecipeResponse::id)
                .returns(expected.name(), RecipeResponse::name)
                .returns(expected.servings(), RecipeResponse::servings)
                .returns(expected.ingredients(), RecipeResponse::ingredients)
                .returns(expected.instructions(), RecipeResponse::instructions)
                .returns(expected.createdDate(), RecipeResponse::createdDate);

        assertThat(responseBody.lastModifiedDate()).isNotNull();
    }

    @Test
    void testUpdateRecipe_NotFound() {
        final UUID recipeId = UUID.randomUUID();

        final UpdateRecipeRequest request = new UpdateRecipeRequest(
                faker.food().dish(),
                faker.bool().bool(),
                faker.number().randomDigit(),
                IntStream.rangeClosed(0, 10).mapToObj(i -> faker.food().ingredient()).collect(Collectors.toSet()),
                "Test Instraction"
        );

        // Perform the PUT request
        final ResponseEntity<ProblemDetail> response = this.testRestTemplate.exchange("/recipes/{id}", HttpMethod.PUT, new HttpEntity<>(request), ProblemDetail.class, recipeId);

        // Verify the response status code and body
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(Objects.requireNonNull(response.getBody()).getDetail()).isEqualTo("Recipe not found by id: " + recipeId);
    }

    @Test
    void testUpdateRecipe_InvalidRequest_BlankName() {
        final UUID recipeId = UUID.fromString("28aa5a21-9940-4db2-adb1-41d2c6528af2");

        // Prepare the request body with blank name
        final UpdateRecipeRequest request = new UpdateRecipeRequest("", true, 4, Set.of("Ingredient 1", "Ingredient 2"), "Instructions");

        // Perform the PUT request
        final ResponseEntity<ProblemDetail> response = this.testRestTemplate.exchange("/recipes/{id}", HttpMethod.PUT, new HttpEntity<>(request), ProblemDetail.class, recipeId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getDetail()).isEqualTo("Wrong fields : name must not be blank");
    }

    @Test
    void testUpdateRecipe_InvalidRequest_NullInstructions() {
        final UUID recipeId = UUID.fromString("28aa5a21-9940-4db2-adb1-41d2c6528af2");

        // Prepare the request body with null instructions
        final UpdateRecipeRequest request = new UpdateRecipeRequest("Recipe 1", true, 4, Set.of("Ingredient 1", "Ingredient 2"), null);

        // Perform the PUT request
        final ResponseEntity<ProblemDetail> response = this.testRestTemplate.exchange("/recipes/{id}", HttpMethod.PUT, new HttpEntity<>(request), ProblemDetail.class, recipeId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getDetail()).contains("Wrong fields", "instructions must not be blank", "instructions must not be null");
    }

    @Test
    void testUpdateRecipe_InvalidRequest_InvalidServings() {
        final UUID recipeId = UUID.fromString("28aa5a21-9940-4db2-adb1-41d2c6528af2");

        // Prepare the request body with null instructions
        final UpdateRecipeRequest request = new UpdateRecipeRequest("Recipe 1", false, 0, Set.of("Ingredient 1", "Ingredient 2"), null);

        // Perform the PUT request
        final ResponseEntity<ProblemDetail> response = this.testRestTemplate.exchange("/recipes/{id}", HttpMethod.PUT, new HttpEntity<>(request), ProblemDetail.class, recipeId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getDetail()).contains("Wrong fields", "instructions must not be blank", "instructions must not be null");
    }

    @Test
    void testDeleteRecipe_Success() {
        final UUID recipeId = UUID.fromString("28aa5a21-9940-4db2-adb1-41d2c6528af2");

        // Perform the DELETE request
        final ResponseEntity<Void> response = this.testRestTemplate.exchange("/recipes/{id}", HttpMethod.DELETE, null, Void.class, recipeId);

        // Verify the response status code
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);

    }

    @Test
    void testDeleteRecipe_NotFound() {
        final UUID recipeId = UUID.fromString("ede99780-5092-4e5b-a867-b9681152b016");

        // Perform the DELETE request and verify the exception
        final ResponseEntity<ProblemDetail> responseEntity = this.testRestTemplate.exchange("/recipes/{id}", HttpMethod.DELETE, null, ProblemDetail.class, recipeId);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody().getDetail()).isEqualTo("Recipe not found by id: " + recipeId);
    }
}
