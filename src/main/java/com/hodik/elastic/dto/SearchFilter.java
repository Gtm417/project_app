package com.hodik.elastic.dto;

import com.hodik.elastic.util.Operations;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

@Data
@Builder
@AllArgsConstructor

public class SearchFilter {


    private final String column;

    private final Operations operations;

    private final List<?> values;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("column", column)
                .append("operations", operations)
                .append("values", values)
                .toString();
    }
}

