package com.hodik.elastic.repositories.search.builder;

import com.hodik.elastic.dto.SearchCriteriaDto;
import com.hodik.elastic.dto.SearchFilter;
import com.hodik.elastic.dto.SearchSort;
import com.hodik.elastic.mappers.PageableMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;

import java.util.List;

import static com.hodik.elastic.util.Operations.LIKE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EsQueryBuilderTest {
    private final SearchSort searchSort = new SearchSort("Name", true);
    private final List<SearchSort> searchSortList = List.of(searchSort);
    private final SearchFilter searchFilter = new SearchFilter("Name", LIKE, List.of("Name"));
    private final SearchCriteriaDto searchCriteriaDto = new SearchCriteriaDto(List.of(searchFilter), 0, 2, searchSortList);
    @Mock
    private PageableMapper pageableMapper;
    @InjectMocks
    private EsQueryBuilder queryBuilder;

    @Test
    void getCriteriaQuery() {
        //given
        when(pageableMapper.getPageable(any())).thenCallRealMethod();
        //when
        CriteriaQuery criteriaQuery = queryBuilder.getCriteriaQuery(searchCriteriaDto);
        //then
        assertNotNull(criteriaQuery);

    }
}