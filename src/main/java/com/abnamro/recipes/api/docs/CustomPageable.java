package com.abnamro.recipes.api.docs;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Parameters({
        @Parameter(
                name = "page", description = "Zero-based page index (0..N)",
                in = ParameterIn.QUERY, schema = @Schema(type = "integer", defaultValue = "0")
        ),
        @Parameter(
                name = "size",
                description = "The size of the page to be returned",
                in = ParameterIn.QUERY, schema = @Schema(type = "integer", defaultValue = "25")
        ),
        @Parameter(
                name = "sort", description = "Sorting criteria in the format: property,(asc|desc). " +
                "Multiple sort criteria are supported.",
                in = ParameterIn.QUERY, schema = @Schema(type = "string", defaultValue = "lastModifiedDate,DESC")
        )
})
public @interface CustomPageable {
}