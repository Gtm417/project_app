package com.hodik.elastic.repositories.search.builder;

import com.hodik.elastic.dto.SearchCriteriaDto;
import com.hodik.elastic.dto.SearchFilter;
import com.hodik.elastic.mappers.SortOrdersMapper;
import com.hodik.elastic.util.Operations;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.client.erhlc.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EsQueryBuilder {
    private final SortOrdersMapper sortOrdersMapper;

    @Autowired
    public EsQueryBuilder(SortOrdersMapper sortOrdersMapper) {
        this.sortOrdersMapper = sortOrdersMapper;
    }

    public CriteriaQuery getCriteriaQuery(SearchCriteriaDto searchCriteriaDto) {
        Criteria criteria = new Criteria();
        List<SearchFilter> filters = searchCriteriaDto.getFilters();
        for (SearchFilter filter : filters) {
            String column = filter.getColumn();
            Operations operation = filter.getOperations();
            List<?> values = filter.getValues();
            //todo smth with list
            Object value = values.get(0);

            switch (operation) {
                case LIKE -> criteria.and(new Criteria(column).contains(value.toString()));
                case EQUAL -> criteria.and(new Criteria(column).matches(value));
                case MORE_THEN -> criteria.and(new Criteria(column).greaterThanEqual(value));
                case LESS_THEN -> criteria.and(new Criteria(column).lessThanEqual(value));
                case FULL_TEXT -> criteria.and(new Criteria(column).fuzzy(value.toString()));
            }
        }

        Pageable pageable = getPageable(searchCriteriaDto);

        CriteriaQuery criteriaQuery = new CriteriaQuery(criteria, pageable);
        return criteriaQuery;
    }

    private Pageable getPageable(SearchCriteriaDto searchCriteriaDto) {
        int page = searchCriteriaDto.getPage();
        int size = searchCriteriaDto.getSize();
        List<Sort.Order> orders = sortOrdersMapper.mapToSortOrder(searchCriteriaDto.getSorts());
        Sort sort = Sort.by(orders);
        return PageRequest.of(page, size, sort);
    }
}
