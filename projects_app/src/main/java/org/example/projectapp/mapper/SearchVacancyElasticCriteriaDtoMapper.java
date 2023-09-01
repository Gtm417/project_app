package org.example.projectapp.mapper;

import org.example.projectapp.controller.dto.SearchDto;
import org.example.projectapp.mapper.dto.ElasticFilterDto;
import org.example.projectapp.mapper.dto.ElasticOperation;
import org.example.projectapp.mapper.dto.SearchElasticCriteriaDto;
import org.springframework.stereotype.Component;

import java.util.List;

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
        List<ElasticFilterDto> filterDtoList = getElasticFilterDtos(searchDto, filterDtoMapper);
        addSearchToFilterDtoList(searchDto, filterDtoList);

        return getElasticCriteriaDto(searchDto, filterDtoList);
    }

    private void addSearchToFilterDtoList(SearchDto searchDto, List<ElasticFilterDto> filterDtoList) {
        String search = searchDto.getSearch();
        if (search != null && !search.isBlank()) {
            boolean orPredicate = true;

            filterDtoList.add(getFilter(search, ABOUT_PROJECT, ElasticOperation.FULL_TEXT, orPredicate));
            filterDtoList.add(getFilter(search, JOB_POSITION, ElasticOperation.FULL_TEXT, orPredicate));
            filterDtoList.add(getFilter(search, DESCRIPTION, ElasticOperation.FULL_TEXT, orPredicate));
        }
    }
}

