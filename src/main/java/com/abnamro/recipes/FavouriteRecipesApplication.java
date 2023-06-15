package com.abnamro.recipes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class FavouriteRecipesApplication {

    public static void main(final String[] args) {
        SpringApplication.run(FavouriteRecipesApplication.class, args);
    }

}
