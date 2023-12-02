package com.hodik.elastic.mapper;

import com.hodik.elastic.dto.FilterDto;
import com.hodik.elastic.dto.Operation;
import com.hodik.elastic.dto.SearchCriteriaDto;
import com.hodik.elastic.dto.SearchDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("searchProjectCriteriaDtoMapper")
public class SearchProjectCriteriaDtoMapper implements SearchCriteriaDtoMapper {
    public static final String NAME = "name";
    public static final String CATEGORY = "category";
    public static final String DESCRIPTION = "description";
    private final FilterDtoMapper filterDtoMapper;


    public SearchProjectCriteriaDtoMapper(FilterDtoMapper mapper) {
        this.filterDtoMapper = mapper;
    }

    public SearchCriteriaDto convertToSearchCriteriaDto(SearchDto searchDto) {
        List<FilterDto> filterDtoList = getElasticFilterDtos(searchDto, filterDtoMapper);
        addSearchToFilterDtoList(searchDto, filterDtoList);

        return getElasticCriteriaDto(searchDto, filterDtoList);
    }

    private void addSearchToFilterDtoList(SearchDto searchDto, List<FilterDto> filterDtoList) {
        String search = searchDto.getSearch();
        if (search != null && !search.isBlank()) {
            boolean orPredicate = true;

            filterDtoList.add(getFilter(search, NAME, Operation.FULL_TEXT, orPredicate));
            filterDtoList.add(getFilter(search, CATEGORY, Operation.FULL_TEXT, orPredicate));
            filterDtoList.add(getFilter(search, DESCRIPTION, Operation.FULL_TEXT, orPredicate));
        }
    }
}
