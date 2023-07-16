package com.hodik.elastic.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchCriteriaDto {
    private List<SearchFilter> filters;
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

