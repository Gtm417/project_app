package org.example.projectapp.service;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Builder
@Getter
@Setter
public class SearchCriteria {
    private String key;
    private String operation;
    private final List<Object> values;
    private boolean orPredicate;

    public SearchCriteria(String key, String operation, List<Object> values) {
        this.key = key;
        this.operation = operation;
        this.values = values;
    }

    public SearchCriteria(String key, String operation, List<Object> values, boolean orPredicate) {
        this(key, operation, values);
        this.orPredicate = orPredicate;
    }
}
