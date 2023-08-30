package org.example.projectapp.mapper;

import org.example.projectapp.controller.dto.FilterDto;
import org.example.projectapp.controller.dto.SearchDto;
import org.example.projectapp.mapper.dto.ElasticFilterDto;
import org.example.projectapp.mapper.dto.ElasticOperation;
import org.example.projectapp.mapper.dto.SearchElasticCriteriaDto;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SearchProjectElasticCriteriaDtoMapper implements SearchElasticCriteriaDtoMapper {
    public static final String NAME = "name";
    public static final String CATEGORY = "category";
    public static final String DESCRIPTION = "description";
    private final ElasticFilterDtoMapper filterDtoMapper;


    public SearchProjectElasticCriteriaDtoMapper(ElasticFilterDtoMapper mapper) {
        this.filterDtoMapper = mapper;
    }

    public SearchElasticCriteriaDto convertToSearchElasticCriteriaDto(SearchDto searchDto) {
        String search = searchDto.getSearch();
        List<FilterDto> filters = searchDto.getFilters();
        if (filters == null) {
            filters = Collections.emptyList();
        }
        List<ElasticFilterDto> filterDtoList = filters.stream()
                .map(filterDtoMapper::convertToElasticFilterDto)
                .collect(Collectors.toList());
        if (search != null && !search.isBlank()) {
            boolean orPredicate = true;

            filterDtoList.add(getFilter(search, NAME, ElasticOperation.FULL_TEXT, orPredicate));
            filterDtoList.add(getFilter(search, CATEGORY, ElasticOperation.FULL_TEXT, orPredicate));
            filterDtoList.add(getFilter(search, DESCRIPTION, ElasticOperation.FULL_TEXT, orPredicate));
        }
        return getElasticCriteriaDto(searchDto, filterDtoList);
    }


}
