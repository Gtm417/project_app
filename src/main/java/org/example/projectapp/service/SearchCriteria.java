package org.example.projectapp.service;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SearchCriteria {
    private String key;
    private String operation;
    private List<Object> values;
    private boolean orPredicate;

    public SearchCriteria(String key, String operation, Object value) {
        this.key = key;
        this.operation = operation;
    }

    public SearchCriteria(String key, String operation, Object value, boolean orPredicate) {
        this(key, operation, value);
        this.orPredicate = orPredicate;
    }
}
