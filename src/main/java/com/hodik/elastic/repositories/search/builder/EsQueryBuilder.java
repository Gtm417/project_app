package com.hodik.elastic.repositories.search.builder;

import com.hodik.elastic.dto.SearchCriteriaDto;
import com.hodik.elastic.dto.SearchFilter;
import com.hodik.elastic.mappers.SortOrdersMapper;
import com.hodik.elastic.util.Operations;
import com.hodik.elastic.util.SearchColumn;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class EsQueryBuilder {
    private final SortOrdersMapper sortOrdersMapper;

    public EsQueryBuilder(SortOrdersMapper sortOrdersMapper) {
        this.sortOrdersMapper = sortOrdersMapper;
    }

    public CriteriaQuery getCriteriaQuery (SearchCriteriaDto searchCriteriaDto){
    Criteria criteria = new Criteria();
    List<SearchFilter> filters = searchCriteriaDto.getFilters();
        for (SearchFilter filter : filters) {
        SearchColumn column = filter.getColumn();
        Operations operation = filter.getOperations();
        List<?> values = filter.getValues();
        //todo smth with list
        Object value = values.get(0);

        switch (operation) {
            case LIKE -> criteria.and(new Criteria(String.valueOf(column)).contains(value.toString()));
            case EQUAL -> criteria.and(new Criteria(String.valueOf(column)).matches(value));
            case MORE_THEN -> criteria.and(new Criteria(String.valueOf(column)).greaterThanEqual(value));
            case LESS_THEN -> criteria.and(new Criteria(String.valueOf(column)).lessThanEqual(value));
            case FULL_TEXT -> criteria.and(new Criteria(String.valueOf(column)).fuzzy(value.toString()));
        }
    }

    Pageable pageable = getPageable(searchCriteriaDto);

        return new CriteriaQuery(criteria, pageable);
}

    private Pageable getPageable(SearchCriteriaDto searchCriteriaDto) {
        int page = searchCriteriaDto.getPage();
        int size = searchCriteriaDto.getSize();
        List<Sort.Order> orders = sortOrdersMapper.mapToSortOrder(searchCriteriaDto.getSorts());
        Sort sort = Sort.by(orders);
        return PageRequest.of(page, size, sort);
    }
}
