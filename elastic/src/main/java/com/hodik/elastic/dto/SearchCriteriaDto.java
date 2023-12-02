package com.hodik.elastic.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchCriteriaDto {

    private List<FilterDto> filters;
    private int page;
    private int size;
    private List<SearchSort> sorts;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("filters", filters)
                .append("page", page)
                .append("size", size)
                .append("sorts", sorts)
                .toString();
    }
}

