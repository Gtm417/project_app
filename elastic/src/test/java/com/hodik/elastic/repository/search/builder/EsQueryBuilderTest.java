package com.hodik.elastic.repository.search.builder;

import com.hodik.elastic.dto.SearchCriteriaDto;
import com.hodik.elastic.dto.SearchFilter;
import com.hodik.elastic.dto.SearchSort;
import com.hodik.elastic.mapper.PageableMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;

import java.util.List;

import static com.hodik.elastic.util.Operations.LIKE;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EsQueryBuilderTest {
    public static final String NAME = "Name";
    public static final int PAGE = 0;
    public static final int SIZE = 2;
    private final SearchSort searchSort = new SearchSort(NAME, true);
    private final List<SearchSort> searchSortList = List.of(searchSort);
    private final SearchFilter searchFilter = new SearchFilter(NAME, LIKE, List.of(NAME));
    private final SearchCriteriaDto searchCriteriaDto = new SearchCriteriaDto(List.of(searchFilter), PAGE, SIZE, searchSortList);
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