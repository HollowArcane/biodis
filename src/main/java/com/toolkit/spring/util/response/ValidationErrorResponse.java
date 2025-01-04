package com.toolkit.spring.util.response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

public class ValidationErrorResponse
{
    private String message = "Invalid data given";
    private Map<String, List<String>> details = new HashMap<>();

    public ValidationErrorResponse(Errors errors)
    {
        for(FieldError error: errors.getFieldErrors())
        {
            List<String> messages = details.computeIfAbsent(error.getField(), key -> new ArrayList<>());
            messages.add(error.getDefaultMessage());
        }
    }

    public String getMessage() {
        return message;
    }

    public Map<String, List<String>> getDetails() {
        return details;
    }
}
