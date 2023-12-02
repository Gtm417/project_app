package com.hodik.elastic.mapper;


import com.hodik.elastic.dto.FilterDto;
import com.hodik.elastic.dto.Operation;
import com.hodik.elastic.dto.SearchCriteriaDto;
import com.hodik.elastic.dto.SearchDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("searchCriteriaDtoMapper")
public class SearchVacancyCriteriaDtoMapper implements SearchCriteriaDtoMapper {
    public static final String ABOUT_PROJECT = "aboutProject";
    public static final String JOB_POSITION = "jobPosition";
    public static final String DESCRIPTION = "description";
    public static final String EXPECTED = "expected";
    private final FilterDtoMapper filterDtoMapper;

    public SearchVacancyCriteriaDtoMapper(FilterDtoMapper filterDtoMapper) {
        this.filterDtoMapper = filterDtoMapper;
    }

    @Override

    public SearchCriteriaDto convertToSearchCriteriaDto(SearchDto searchDto) {
        List<FilterDto> filterDtoList = getElasticFilterDtos(searchDto, filterDtoMapper);
        addSearchToFilterDtoList(searchDto, filterDtoList);

        return getElasticCriteriaDto(searchDto, filterDtoList);
    }

    private void addSearchToFilterDtoList(SearchDto searchDto, List<FilterDto> filterDtoList) {
        String search = searchDto.getSearch();
        if (search != null && !search.isBlank()) {
            boolean orPredicate = true;

            filterDtoList.add(getFilter(search, ABOUT_PROJECT, Operation.FULL_TEXT, orPredicate));
            filterDtoList.add(getFilter(search, JOB_POSITION, Operation.FULL_TEXT, orPredicate));
            filterDtoList.add(getFilter(search, DESCRIPTION, Operation.FULL_TEXT, orPredicate));
            filterDtoList.add(getFilter(search, EXPECTED, Operation.FULL_TEXT, orPredicate));
        }
    }
}

