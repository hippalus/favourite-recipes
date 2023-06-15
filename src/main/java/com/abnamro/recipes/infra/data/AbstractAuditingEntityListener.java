package com.abnamro.recipes.infra.data;

import com.abnamro.recipes.infra.data.entity.AbstractAuditingEntity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component
public class AbstractAuditingEntityListener {

    @PrePersist
    public void prePersist(final AbstractAuditingEntity target) {
        target.setCreatedDate(ZonedDateTime.now());
        this.preUpdate(target);
    }

    @PreUpdate
    public void preUpdate(final AbstractAuditingEntity target) {
        target.setLastModifiedDate(ZonedDateTime.now());
    }
}
