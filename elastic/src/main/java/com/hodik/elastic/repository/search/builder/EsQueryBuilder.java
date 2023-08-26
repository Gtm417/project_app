package com.hodik.elastic.repository.search.builder;

import com.hodik.elastic.dto.FilterDto;
import com.hodik.elastic.dto.Operation;
import com.hodik.elastic.dto.SearchCriteriaDto;
import com.hodik.elastic.mapper.PageableMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EsQueryBuilder {
    private final PageableMapper pageableMapper;

    @Autowired
    public EsQueryBuilder(PageableMapper pageableMapper) {

        this.pageableMapper = pageableMapper;
    }

    public CriteriaQuery getCriteriaQuery(SearchCriteriaDto searchCriteriaDto) {
        Criteria criteria = new Criteria();
        List<FilterDto> filters = searchCriteriaDto.getFilters();
        for (FilterDto filter : filters) {
            String column = filter.getColumn();
            Operation operation = filter.getOperation();
            List<?> values = filter.getValues();
            Object value = values.get(0);

            switch (operation) {
                case LIKE -> criteria.and(new Criteria(column).contains(value.toString()));
                case EQUAL -> criteria.and(new Criteria(column).is(value.toString()));
                case NOT_EQUAL -> criteria.and(new Criteria(column).not().is(value));
                case FULL_TEXT -> criteria.and(new Criteria(column).matches(value));
                case GREATER -> criteria.and(new Criteria(column).greaterThanEqual(value));
                case LESS -> criteria.and(new Criteria(column).lessThanEqual(value));
                case IN -> criteria.and(new Criteria(column).in(values));
            }
        }

        Pageable pageable = pageableMapper.getPageable(searchCriteriaDto);

        return new CriteriaQuery(criteria, pageable);
    }


}
