package org.example.projectapp.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Data
@AllArgsConstructor
public class ValidationErrorResponse {
    private Map<String, Set<String>> errors;

    public ValidationErrorResponse() {
        this.errors = new HashMap<>();
    }
}
