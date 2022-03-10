package org.example.projectapp.controller.dto;

public enum SearchOperation {
    EQUAL("equal"),
    NOT_EQUAL("notEqual"),
    GREATER("more"),
    LESS("less"),
    LIKE("like"),
    IN("in");

    SearchOperation(String name) {
        this.name = name;
    }

    private final String name;

    public String getName() {
        return name;
    }
}
