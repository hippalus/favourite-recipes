package com.abnamro.recipes.infra.data.entity;

import com.abnamro.recipes.infra.data.AbstractAuditingEntityListener;
import com.abnamro.recipes.infra.data.ZonedDateTimeConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AbstractAuditingEntityListener.class)
public abstract class AbstractAuditingEntity {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "created_date", updatable = false)
    @Convert(converter = ZonedDateTimeConverter.class)
    private ZonedDateTime createdDate;

    @Column(name = "last_modified_date")
    @Convert(converter = ZonedDateTimeConverter.class)
    private ZonedDateTime lastModifiedDate;

}