package com.hodik.performance.test.app.dto;

import java.time.LocalDateTime;

public enum DataType {
    STRING(String.class), DATE_TIME(LocalDateTime.class);

    private final Class<?> clazz;

    DataType(Class<?> clazz) {
        this.clazz = clazz;
    }

    public Class<?> getClazz() {
        return clazz;
    }
}
