package com.abnamro.recipes.infra.data.specification;

import com.abnamro.recipes.domain.model.RecipeSearchCriteria;
import com.abnamro.recipes.infra.data.entity.RecipeEntity;
import jakarta.persistence.criteria.*;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public record RecipeSpecification(RecipeSearchCriteria searchCriteria) implements Specification<RecipeEntity> {

    @Override
    public Predicate toPredicate(final Root<RecipeEntity> root, final CriteriaQuery<?> query, final CriteriaBuilder criteriaBuilder) {
        final List<Predicate> predicates = new ArrayList<>();

        if (this.searchCriteria.isVegetarian()) {
            predicates.add(criteriaBuilder.isTrue(root.get("vegetarian")));
        }

        if (Objects.nonNull(this.searchCriteria.servings()) && this.searchCriteria.servings() > 0) {
            predicates.add(criteriaBuilder.equal(root.get("servings"), this.searchCriteria.servings()));
        }

        if (CollectionUtils.isNotEmpty(this.searchCriteria.includedIngredients())) {
            final Join<RecipeEntity, String> ingredientJoin = root.joinSet("ingredients", JoinType.INNER);
            predicates.add(ingredientJoin.in(this.searchCriteria.includedIngredients()));
        }

        if (CollectionUtils.isNotEmpty(this.searchCriteria.excludedIngredients())) {
            final Join<RecipeEntity, String> ingredientJoin = root.joinSet("ingredients", JoinType.INNER);
            predicates.add(criteriaBuilder.not(ingredientJoin.in(this.searchCriteria.excludedIngredients())));
        }

        if (this.searchCriteria.searchText() != null && !this.searchCriteria.searchText().isBlank()) {
            predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("instructions")),
                    "%" + this.searchCriteria.searchText().toLowerCase() + "%")  //TODO: use postgresql full text search
            );
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
