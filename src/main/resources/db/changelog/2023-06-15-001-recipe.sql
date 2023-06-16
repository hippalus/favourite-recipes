-- liquibase formatted sql
-- changeset hakan.isler:create.recipe

CREATE TABLE recipe
(
    id                 UUID    NOT NULL,
    name               VARCHAR(255),
    is_vegetarian      BOOLEAN NOT NULL,
    servings           INTEGER,
    instructions       TEXT,
    created_date       TIMESTAMP WITHOUT TIME ZONE,
    last_modified_date TIMESTAMP WITHOUT TIME ZONE,
    PRIMARY KEY (id)
);

CREATE TABLE recipe_ingredients
(
    recipe_id  UUID NOT NULL,
    ingredient VARCHAR(255)
);

ALTER TABLE recipe_ingredients
    ADD CONSTRAINT FK_recipe_ingredients_recipe_id
        FOREIGN KEY (recipe_id)
            REFERENCES recipe (id);