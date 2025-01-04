package com.toolkit.spring.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.toolkit.spring.validator.UniqueValidator;

@Constraint(validatedBy = { UniqueValidator.class })
@Target({ ElementType.FIELD, ElementType.PARAMETER }) // Can be applied to fields or method parameters
@Retention(RetentionPolicy.RUNTIME)
public @interface Unique
{
    String message() default "The value already exists inside database"; // Default error message

    Class<?>[] groups() default {}; // For grouping constraints

    Class<? extends Payload>[] payload() default {}; // For additional metadata

    String table(); // Name of the table to check

    String column(); // Name of the column to check
}
