package com.hodik.elastic.mapper;

import com.hodik.elastic.dto.*;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public interface SearchCriteriaDtoMapper {

    SearchCriteriaDto convertToSearchCriteriaDto(SearchDto searchDto);

    default FilterDto getFilter(String search, String column, Operation operation, boolean orPredicate) {
        return FilterDto.builder()
                .column(column)
                .values(List.of(search))
                .operation(operation)
                .orPredicate(orPredicate)
                .build();
    }


    default SearchCriteriaDto getElasticCriteriaDto(SearchDto searchDto, List<FilterDto> filterDtoList) {
        return SearchCriteriaDto.builder()
                .filters(filterDtoList)
                .size(searchDto.getSize())
                .sorts(searchDto.getSorts())
                .page(searchDto.getPage())
                .build();
    }

    default List<SearchFilterDto> getFilterDtoList(SearchDto searchDto) {
        List<SearchFilterDto> filters = searchDto.getFilters();
        if (filters == null) {
            filters = Collections.emptyList();
        }
        setDefaultValuesIfNeed(searchDto);
        return filters;
    }

    default void setDefaultValuesIfNeed(SearchDto searchDto) {
        searchDto.setSize(Objects.requireNonNullElse(searchDto.getSize(), 10));
        searchDto.setPage(Objects.requireNonNullElse(searchDto.getPage(), 0));
        searchDto.setSorts(Objects.requireNonNullElse(searchDto.getSorts(), Collections.emptyList()));
    }

    default List<FilterDto> getElasticFilterDtos(SearchDto searchDto, FilterDtoMapper filterDtoMapper) {
        List<SearchFilterDto> filters = getFilterDtoList(searchDto);
        return filters.stream()
                .map(filterDtoMapper::convertToFilterDto)
                .collect(Collectors.toList());
    }
}
