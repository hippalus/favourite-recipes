package com.abnamro.recipes;

import com.abnamro.recipes.common.IntegrationTestConfiguration;
import org.springframework.boot.SpringApplication;

public class TestFavouriteRecipesApplication {

    public static void main(final String[] args) {
        SpringApplication.from(FavouriteRecipesApplication::main)
                .with(IntegrationTestConfiguration.class)
                .run(args);
    }

}
