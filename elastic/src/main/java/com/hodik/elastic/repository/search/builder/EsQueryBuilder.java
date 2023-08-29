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
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class EsQueryBuilder {
    private final PageableMapper pageableMapper;

    @Autowired
    public EsQueryBuilder(PageableMapper pageableMapper) {

        this.pageableMapper = pageableMapper;
    }


    public CriteriaQuery getCriteriaQuery(SearchCriteriaDto searchCriteriaDto) {
        Map<Boolean, List<FilterDto>> filters = searchCriteriaDto.getFilters().stream()
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(FilterDto::isOrPredicate));

        Criteria criteria;
        Pageable pageable = pageableMapper.getPageable(searchCriteriaDto);
        List<Criteria> andCriteriaList = getCriteriaList(filters.get(false));
        List<Criteria> orCriteriaList = getCriteriaList(filters.get(true));

        Criteria andCriteria = getAndOrCriteria(andCriteriaList, false);
        Criteria orCriteria = getAndOrCriteria(orCriteriaList, true);

        criteria = andCriteria.subCriteria(orCriteria);
        return new CriteriaQuery(criteria, pageable);

    }

    private Criteria getAndOrCriteria(List<Criteria> criteriaList, boolean orPredicate) {
        Criteria criteria = new Criteria();
        for (Criteria cr : criteriaList) {
            if (!orPredicate) {
                criteria = criteria.and(cr);
            } else {
                criteria = criteria.or(cr);
            }
        }
        return criteria;
    }


    private List<Criteria> getCriteriaList(List<FilterDto> filters) {
        List<Criteria> criteriaList = new ArrayList<>();
        if (CollectionUtils.isEmpty(filters)) {
            return Collections.emptyList();
        }
        for (FilterDto filter : filters) {
            String column = filter.getColumn();
            Operation operation = filter.getOperation();
            List<?> values = filter.getValues();

            criteriaList.add(getCriteriaToAdd(column, operation, values));
        }
        return criteriaList;
    }

    private Criteria getCriteriaToAdd(String column, Operation operation, List<?> values) {
        Criteria criteriaToAdd = null;
        Object value = values.get(0);
        switch (operation) {
            case LIKE:
                criteriaToAdd = new Criteria(column).contains(value.toString());
                break;
            case EQUAL:
                criteriaToAdd = new Criteria(column).is(value.toString());
                break;
            case NOT_EQUAL:
                criteriaToAdd = new Criteria(column).not().is(value);
                break;
            case FULL_TEXT:
                criteriaToAdd = new Criteria(column).matches(value);
                break;
            case GREATER:
                criteriaToAdd = new Criteria(column).greaterThanEqual(value);
                break;
            case LESS:
                criteriaToAdd = new Criteria(column).lessThanEqual(value);
                break;
            case IN:
                criteriaToAdd = new Criteria(column).in(values);
                break;
        }
        return criteriaToAdd;

    }


}
