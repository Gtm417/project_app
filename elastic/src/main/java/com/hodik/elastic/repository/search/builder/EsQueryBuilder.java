package com.hodik.elastic.repository.search.builder;

import com.hodik.elastic.dto.FilterDto;
import com.hodik.elastic.dto.Operation;
import com.hodik.elastic.dto.SearchCriteriaDto;
import com.hodik.elastic.mapper.PageableMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Component;

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
        Map<Boolean, List<FilterDto>> filters = searchCriteriaDto.getFilters()
                .stream()
                .filter(Objects::nonNull)
                .filter(filterDto -> CollectionUtils.isNotEmpty(filterDto.getValues()))
                .filter(filterDto -> Objects.nonNull(filterDto.getValues().get(0)))
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
        if (criteriaList.isEmpty()) {
            return new Criteria();
        }
        return criteriaList.stream()
                .reduce(orPredicate ? Criteria::or : Criteria::and)
                .orElseThrow();
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
            if (CollectionUtils.isEmpty(values)) {
                continue;
            }
            criteriaList.add(getCriteriaToAdd(column, operation, values));
        }

        return criteriaList;
    }

    private Criteria getCriteriaToAdd(String column, Operation operation, List<?> values) {
        Criteria criteriaToAdd;
        Object value = values.get(0);
//        if (value == null) {
//            return new Criteria();
//        }
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
            default:
                throw new IllegalArgumentException("Operation " + operation + "not found");
        }
        return criteriaToAdd;

    }


}
