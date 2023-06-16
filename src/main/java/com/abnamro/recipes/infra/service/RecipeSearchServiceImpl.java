package com.abnamro.recipes.infra.service;

import com.abnamro.recipes.domain.model.Recipe;
import com.abnamro.recipes.domain.model.RecipeSearchCriteria;
import com.abnamro.recipes.domain.service.RecipeSearchService;
import com.abnamro.recipes.infra.data.entity.RecipeEntity;
import com.abnamro.recipes.infra.data.repository.RecipeJpaRepository;
import com.abnamro.recipes.infra.data.specification.RecipeSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RecipeSearchServiceImpl implements RecipeSearchService {

    private final RecipeJpaRepository recipeJpaRepository;

    @Override
    public List<Recipe> searchRecipes(final RecipeSearchCriteria searchCriteria) {
        final RecipeSpecification specification = new RecipeSpecification(searchCriteria);

        return this.recipeJpaRepository.findAll(specification, searchCriteria.pageable())
                .stream()
                .map(RecipeEntity::toModel)
                .toList();
    }
}