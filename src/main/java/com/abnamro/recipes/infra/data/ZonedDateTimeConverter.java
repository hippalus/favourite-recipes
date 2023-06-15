package com.abnamro.recipes.infra.data;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Converter
public class ZonedDateTimeConverter implements AttributeConverter<ZonedDateTime, Timestamp> {

    @Override
    public Timestamp convertToDatabaseColumn(final ZonedDateTime zonedDateTime) {
        if (zonedDateTime == null) {
            return null;
        }
        final LocalDateTime localDateTime = zonedDateTime.toLocalDateTime();
        final Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        return Timestamp.from(instant);
    }

    @Override
    public ZonedDateTime convertToEntityAttribute(final Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        }
        final Instant instant = timestamp.toInstant();
        return instant.atZone(ZoneId.systemDefault());
    }
}