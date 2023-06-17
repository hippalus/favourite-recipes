package com.abnamro.recipes.common;

import com.abnamro.recipes.domain.model.Recipe;
import com.abnamro.recipes.infra.data.entity.RecipeEntity;
import com.github.javafaker.Faker;
import org.jetbrains.annotations.NotNull;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class TestFixture {

    private TestFixture() {
    }

    private static final Faker faker = new Faker();

    public static List<Recipe> getRecipes() {
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

    @NotNull
    public static TestRecipes createRecipes() {
        final Recipe recipe = Recipe.builder()
                .name(faker.food().dish())
                .vegetarian(faker.bool().bool())
                .ingredients(IntStream.rangeClosed(0, 10).mapToObj(i -> faker.food().ingredient()).collect(Collectors.toSet()))
                .servings(faker.number().randomDigit())
                .instructions(faker.shakespeare().romeoAndJulietQuote())
                .build();

        final RecipeEntity recipeEntity = new RecipeEntity();
        recipeEntity.setId(UUID.randomUUID());
        recipeEntity.setName(recipe.name());
        recipeEntity.setServings(recipe.servings());
        recipeEntity.setIngredients(recipe.ingredients());
        recipeEntity.setInstructions(recipe.instructions());

        return new TestRecipes(recipe, recipeEntity);
    }

    public record TestRecipes(Recipe recipe, RecipeEntity recipeEntity) {
    }
}
