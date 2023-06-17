truncate table public.recipe restart identity cascade;
truncate table public.recipe_ingredients restart identity cascade;
COMMIT;

INSERT INTO public.recipe (id, name, vegetarian, servings, instructions, created_date, last_modified_date)
VALUES ('28aa5a21-9940-4db2-adb1-41d2c6528af2', 'Recipe 1', true, 4, 'Instructions for Recipe 1',
        '2023-06-15 15:12:42.791951', '2023-06-15 15:12:42.791971');

INSERT INTO public.recipe (id, name, vegetarian, servings, instructions, created_date, last_modified_date)
VALUES ('6e4ab6fc-37de-4196-b992-902443745275', 'Recipe 2', false, 2, 'Instructions for Recipe 2',
        '2023-06-15 15:27:59.001520', '2023-06-15 15:27:59.001560');

INSERT INTO public.recipe (id, name, vegetarian, servings, instructions, created_date, last_modified_date)
VALUES ('12988cfc-7547-45a4-8ed6-7a6379eb078d', 'Recipe 3', true, 3, 'Instructions for Recipe 3',
        '2023-06-15 15:30:27.343721', '2023-06-15 15:30:27.343750');

INSERT INTO public.recipe (id, name, vegetarian, servings, instructions, created_date, last_modified_date)
VALUES ('30d0ffa3-6b33-4925-bef0-4868708e06ce', 'Recipe 4', false, 5, 'Instructions for Recipe 4',
        '2023-06-15 15:30:31.291956', '2023-06-15 15:30:31.292043');

INSERT INTO public.recipe (id, name, vegetarian, servings, instructions, created_date, last_modified_date)
VALUES ('a26bc586-bf65-4a21-8732-7bbadab63464', 'Recipe 5', true, 2, 'Instructions for Recipe 5',
        '2023-06-15 17:31:19.799439', '2023-06-15 17:31:19.799459');

INSERT INTO public.recipe (id, name, vegetarian, servings, instructions, created_date, last_modified_date)
VALUES ('6ebf7c4d-7bb7-4a16-82c2-f4ac93222bbc', 'Recipe 6', true, 4, 'Instructions for Recipe 6',
        '2023-06-15 17:33:06.468289', '2023-06-15 17:33:06.468427');

INSERT INTO public.recipe (id, name, vegetarian, servings, instructions, created_date, last_modified_date)
VALUES ('183c75b3-6ec3-43cd-a5d8-3f12173df80d', 'Recipe 7', false, 3, 'Instructions for Recipe 7',
        '2023-06-15 23:34:43.568467', '2023-06-15 23:34:43.568488');


INSERT INTO public.recipe_ingredients (recipe_id, ingredient)
VALUES ('28aa5a21-9940-4db2-adb1-41d2c6528af2', 'Ingredient 1');

INSERT INTO public.recipe_ingredients (recipe_id, ingredient)
VALUES ('6e4ab6fc-37de-4196-b992-902443745275', 'Ingredient 2');

INSERT INTO public.recipe_ingredients (recipe_id, ingredient)
VALUES ('12988cfc-7547-45a4-8ed6-7a6379eb078d', 'Ingredient 3');

INSERT INTO public.recipe_ingredients (recipe_id, ingredient)
VALUES ('30d0ffa3-6b33-4925-bef0-4868708e06ce', 'Ingredient 4');

INSERT INTO public.recipe_ingredients (recipe_id, ingredient)
VALUES ('a26bc586-bf65-4a21-8732-7bbadab63464', 'Ingredient 5');

INSERT INTO public.recipe_ingredients (recipe_id, ingredient)
VALUES ('6ebf7c4d-7bb7-4a16-82c2-f4ac93222bbc', 'Ingredient 6');

INSERT INTO public.recipe_ingredients (recipe_id, ingredient)
VALUES ('183c75b3-6ec3-43cd-a5d8-3f12173df80d', 'Ingredient 7');


COMMIT;