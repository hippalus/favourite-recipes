package com.abnamro.recipes;

import com.abnamro.recipes.common.IntegrationTestConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(IntegrationTestConfiguration.class)
class FavouriteRecipesApplicationTests {

    @Test
    void contextLoads() {
    }

}
