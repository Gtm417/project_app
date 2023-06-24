package com.hodik.elastic.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor


public class SearchCriteriaDto {
    private List<SearchFilter> filters;
    private int page;
    private int size;
    private List<SearchSort> sorts;
}

