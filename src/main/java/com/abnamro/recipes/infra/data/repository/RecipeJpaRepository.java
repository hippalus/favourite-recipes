package com.abnamro.recipes.infra.data.repository;

import com.abnamro.recipes.infra.data.entity.RecipeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RecipeJpaRepository extends JpaRepository<RecipeEntity, UUID>, JpaSpecificationExecutor<RecipeEntity> {
}