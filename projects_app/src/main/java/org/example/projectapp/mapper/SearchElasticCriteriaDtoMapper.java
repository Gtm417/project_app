package org.example.projectapp.mapper;

import org.apache.commons.lang3.StringUtils;
import org.example.projectapp.controller.dto.FilterDto;
import org.example.projectapp.controller.dto.SearchDto;
import org.example.projectapp.mapper.dto.ElasticFilterDto;
import org.example.projectapp.mapper.dto.ElasticOperation;
import org.example.projectapp.mapper.dto.SearchElasticCriteriaDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SearchElasticCriteriaDtoMapper {
    private final ElasticFilterDtoMapper filterDtoMapper;
    private List<ElasticFilterDto> filterDtoList;

    public SearchElasticCriteriaDtoMapper(ElasticFilterDtoMapper mapper) {
        this.filterDtoMapper = mapper;
    }

    public SearchElasticCriteriaDto convertToSearchElasticCriteriaDto(SearchDto searchDto) {
        String search = searchDto.getSearch();
        List<FilterDto> filters = searchDto.getFilters();
        filterDtoList = filters.stream()
                .map(filterDtoMapper::convertToProjectElasticFilterDto)
                .collect(Collectors.toList());
        if (StringUtils.isNotBlank(search)) {
            boolean orPredicate = true;
            addNameFilters(search, orPredicate);
            addFilters(search, "category", ElasticOperation.FULL_TEXT, orPredicate);
            addFilters(search, "description", ElasticOperation.FULL_TEXT, orPredicate);
        }
        return getElasticCriteriaDto(searchDto);
    }

    private void addNameFilters(String search, boolean orPredicate) {
        if (StringUtils.contains(" ", search)) {
            addFilters(search, "name", ElasticOperation.FULL_TEXT, orPredicate);
        } else {
            addFilters(search, "name", ElasticOperation.LIKE, orPredicate);
        }
    }

    private SearchElasticCriteriaDto getElasticCriteriaDto(SearchDto searchDto) {
        return SearchElasticCriteriaDto.builder()
                .filters(filterDtoList)
                .size(searchDto.getSize())
                .sorts(searchDto.getSorts())
                .page(searchDto.getPage())
                .build();
    }

    private void addFilters(String search, String column, ElasticOperation operation, boolean orPredicate) {
        ElasticFilterDto elasticFilterDto = ElasticFilterDto.builder()
                .column(column)
                .values(List.of(search))
                .operation(operation)
                .orPredicate(orPredicate)
                .build();
        filterDtoList.add(elasticFilterDto);
    }
}
