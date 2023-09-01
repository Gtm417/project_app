package com.hodik.elastic.util;

import org.springframework.data.elasticsearch.core.mapping.PropertyValueConverter;
import org.springframework.lang.NonNull;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;


public class CustomDatePropertyValueConverter implements PropertyValueConverter {
    @Override
    @NonNull
    public Object write(Object value) {
        if (value.getClass().isInstance("String")) {
            value = LocalDateTime.parse(value.toString());
        }
        LocalDateTime localDateTime = (LocalDateTime) value;
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().getEpochSecond();
    }

    @Override
    public Object read(Object value) {
        if (value instanceof Number) {
            long dateInMillis = ((Number) value).longValue();
            return LocalDateTime.ofInstant(Instant.ofEpochSecond(dateInMillis), ZoneOffset.UTC);
        } else {
            throw new IllegalArgumentException("Unexpected timestamp value " + value);
        }

    }
}
