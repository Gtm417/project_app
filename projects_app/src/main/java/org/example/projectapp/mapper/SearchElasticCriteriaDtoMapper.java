package org.example.projectapp.mapper;

import org.example.projectapp.controller.dto.FilterDto;
import org.example.projectapp.controller.dto.SearchDto;
import org.example.projectapp.mapper.dto.ElasticFilterDto;
import org.example.projectapp.mapper.dto.ElasticOperation;
import org.example.projectapp.mapper.dto.SearchElasticCriteriaDto;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public interface SearchElasticCriteriaDtoMapper {

    SearchElasticCriteriaDto convertToSearchElasticCriteriaDto(SearchDto searchDto);

    default ElasticFilterDto getFilter(String search, String column, ElasticOperation operation, boolean orPredicate) {
        return ElasticFilterDto.builder()
                .column(column)
                .values(List.of(search))
                .operation(operation)
                .orPredicate(orPredicate)
                .build();
    }


    default SearchElasticCriteriaDto getElasticCriteriaDto(SearchDto searchDto, List<ElasticFilterDto> filterDtoList) {
        return SearchElasticCriteriaDto.builder()
                .filters(filterDtoList)
                .size(searchDto.getSize())
                .sorts(searchDto.getSorts())
                .page(searchDto.getPage())
                .build();
    }

    default List<FilterDto> getFilterDtoList(SearchDto searchDto) {
        List<FilterDto> filters = searchDto.getFilters();
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

    default List<ElasticFilterDto> getElasticFilterDtos(SearchDto searchDto, ElasticFilterDtoMapper filterDtoMapper) {
        List<FilterDto> filters = getFilterDtoList(searchDto);
        return filters.stream()
                .map(filterDtoMapper::convertToElasticFilterDto)
                .collect(Collectors.toList());
    }
}
