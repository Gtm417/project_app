package com.hodik.elastic.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hodik.elastic.util.SearchFilterDeserializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@JsonDeserialize(using = SearchFilterDeserializer.class)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FilterDto {
    private final String column;

    private final Operation operation;

    private final List<?> values;

    private final Class<?> clazz;
    private final boolean orPredicate;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("column", column)
                .append("operation", operation)
                .append("values", values)
                .append("class", clazz)
                .append("orPredicate", orPredicate)
                .toString();
    }
}

