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
public class SearchVacancyElasticCriteriaDtoMapper implements SearchElasticCriteriaDtoMapper {
    public static final String ABOUT_PROJECT = "aboutProject";
    public static final String JOB_POSITION = "jobPosition";
    public static final String DESCRIPTION = "description";
    private final ElasticFilterDtoMapper filterDtoMapper;

    public SearchVacancyElasticCriteriaDtoMapper(ElasticFilterDtoMapper filterDtoMapper) {
        this.filterDtoMapper = filterDtoMapper;
    }

    @Override
    public SearchElasticCriteriaDto convertToSearchElasticCriteriaDto(SearchDto searchDto) {
        List<ElasticFilterDto> filterDtoList = getElasticFilterDtos(searchDto);
        String search = searchDto.getSearch();
        if (search != null && !search.isBlank()) {
            boolean orPredicate = true;
            filterDtoList.add(getFilter(search, ABOUT_PROJECT, ElasticOperation.FULL_TEXT, orPredicate));
            filterDtoList.add(getFilter(search, JOB_POSITION, ElasticOperation.FULL_TEXT, orPredicate));
            filterDtoList.add(getFilter(search, DESCRIPTION, ElasticOperation.FULL_TEXT, orPredicate));
        }

        return getElasticCriteriaDto(searchDto, filterDtoList);
    }

    private List<ElasticFilterDto> getElasticFilterDtos(SearchDto searchDto) {
        List<ElasticFilterDto> filterDtoList;
        List<FilterDto> filters = searchDto.getFilters();
        if (filters == null) {
            filters = Collections.emptyList();
        }
        filterDtoList = filters.stream()
                .map(filterDtoMapper::convertToElasticFilterDto)
                .collect(Collectors.toList());
        return filterDtoList;
    }
}

