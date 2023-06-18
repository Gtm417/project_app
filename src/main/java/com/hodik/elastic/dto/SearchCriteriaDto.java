package com.hodik.elastic.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor


public class SearchCriteriaDto {
private List<SearchFilter> filters;
}
