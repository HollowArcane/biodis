package com.toolkit.spring.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.toolkit.spring.annotation.Unique;

@Component
public class UniqueValidator implements ConstraintValidator<Unique, Object> {

    @Autowired
    JdbcTemplate jdbcTemplate; // Injected to query the database

    private String table;
    private String column;

    @Override
    public void initialize(Unique constraintAnnotation)
    {
        this.table = constraintAnnotation.table();
        this.column = constraintAnnotation.column();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context)
    {
        if (value == null || jdbcTemplate == null)
        {
            return true; // Skip validation if the ID is null (use @NotNull for that)
        }

        String query = String.format("SELECT COUNT(*) FROM %s WHERE %s = ?", table, column);
        Integer count = jdbcTemplate.queryForObject(query, Integer.class, value);

        return count == null || count == 0; // Valid if the ID exists
    }
}
