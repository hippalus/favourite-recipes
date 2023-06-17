-- liquibase formatted sql
-- changeset hakan.isler:create.recipe

CREATE TABLE recipe
(
    id                 UUID    NOT NULL,
    name               VARCHAR(255),
    vegetarian         BOOLEAN NOT NULL,
    servings           INTEGER NOT NULL,
    instructions       TEXT    NOT NULL,
    created_date       TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    last_modified_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
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