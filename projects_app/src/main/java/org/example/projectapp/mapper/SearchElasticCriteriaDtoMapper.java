package org.example.projectapp.mapper;

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
        filterDtoList = searchDto.getFilters().stream()
                .map(filterDtoMapper::convertToProjectElasticFilterDto)
                .collect(Collectors.toList());
        if (search != null && !search.isBlank()) {
            addFilters(search, "name", ElasticOperation.LIKE);
            addFilters(search, "category", ElasticOperation.LIKE);
            addFilters(search, "description", ElasticOperation.FULL_TEXT);
        }
        return getElasticCriteriaDto(searchDto);
    }

    private SearchElasticCriteriaDto getElasticCriteriaDto(SearchDto searchDto) {
        return SearchElasticCriteriaDto.builder()
                .filters(filterDtoList)
                .size(searchDto.getSize())
                .sorts(searchDto.getSorts())
                .page(searchDto.getPage())
                .build();
    }

    private void addFilters(String search, String column, ElasticOperation operation) {
        ElasticFilterDto elasticFilterDto = ElasticFilterDto.builder()
                .column(column)
                .values(List.of(search))
                .operation(operation).build();
        filterDtoList.add(elasticFilterDto);
    }
}
