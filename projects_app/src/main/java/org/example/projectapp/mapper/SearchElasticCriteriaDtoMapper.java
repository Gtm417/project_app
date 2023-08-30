package org.example.projectapp.mapper;

import org.example.projectapp.controller.dto.SearchDto;
import org.example.projectapp.mapper.dto.ElasticFilterDto;
import org.example.projectapp.mapper.dto.ElasticOperation;
import org.example.projectapp.mapper.dto.SearchElasticCriteriaDto;

import java.util.List;

public interface SearchElasticCriteriaDtoMapper {
    SearchElasticCriteriaDto convertToSearchElasticCriteriaDto(SearchDto searchDto);

    default ElasticFilterDto getFilter(String search, String column, ElasticOperation operation, boolean orPredicate) {
        ElasticFilterDto elasticFilterDto = ElasticFilterDto.builder()
                .column(column)
                .values(List.of(search))
                .operation(operation)
                .orPredicate(orPredicate)
                .build();
        return elasticFilterDto;
    }

    default SearchElasticCriteriaDto getElasticCriteriaDto(SearchDto searchDto, List<ElasticFilterDto> filterDtoList) {
        return SearchElasticCriteriaDto.builder()
                .filters(filterDtoList)
                .size(searchDto.getSize())
                .sorts(searchDto.getSorts())
                .page(searchDto.getPage())
                .build();
    }
}
