package org.example.projectapp.mapper;

import org.example.projectapp.controller.dto.SearchDto;
import org.example.projectapp.mapper.dto.ElasticFilterDto;
import org.example.projectapp.mapper.dto.ElasticOperation;
import org.example.projectapp.mapper.dto.SearchElasticCriteriaDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SearchUserElasticDtoMapper implements SearchElasticCriteriaDtoMapper {
    private final ElasticFilterDtoMapper filterDtoMapper;

    public SearchUserElasticDtoMapper(ElasticFilterDtoMapper filterDtoMapper) {
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
            filterDtoList.add(getFilter(search, "cv", ElasticOperation.FULL_TEXT, orPredicate));
            filterDtoList.add(getFilter(search, "skills.skillName", ElasticOperation.FULL_TEXT, orPredicate));
            filterDtoList.add(getFilter(search, "description", ElasticOperation.FULL_TEXT, orPredicate));
        }
    }
}


