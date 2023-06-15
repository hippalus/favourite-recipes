package com.abnamro.recipes;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(initializers = PostgreSQLContainerInitializer.class)
class FavouriteRecipesApplicationTests {

    @Test
    void contextLoads() {
    }

}
