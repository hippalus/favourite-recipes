package com.abnamro.recipes.infra.data.entity;

import com.abnamro.recipes.domain.model.Recipe;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "recipe")
public class RecipeEntity extends AbstractAuditingEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "is_vegetarian")
    private boolean isVegetarian;

    @Column(name = "servings")
    private Integer servings;

    @Column(name = "ingredient")
    @ElementCollection
    @CollectionTable(name = "recipe_ingredients", joinColumns = @JoinColumn(name = "recipe_id"))
    private Set<String> ingredients = new HashSet<>();

    @Column(name = "instructions")
    private String instructions;

    public Recipe toModel() {
        return new Recipe(
                this.getId(),
                this.name,
                this.isVegetarian,
                this.servings,
                this.ingredients,
                this.instructions,
                this.getCreatedDate(),
                this.getLastModifiedDate()
        );
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        final RecipeEntity that = (RecipeEntity) o;
        return this.getId() != null && Objects.equals(this.getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return this.getClass().hashCode();
    }


}
