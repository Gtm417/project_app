package org.example.projectapp.mapper.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.example.projectapp.util.SearchFilterSerializer;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@JsonSerialize(using = SearchFilterSerializer.class)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ElasticFilterDto {
    private final String column;

    private final ElasticOperation operation;

    private final List<Object> values;

    private final Class<?> clazz;
    private final boolean orPredicate;

    public boolean isOrPredicate() {
        return orPredicate;
    }

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

