package com.hodik.elastic.repositories.impl;


import com.hodik.elastic.dto.SearchCriteriaDto;
import com.hodik.elastic.dto.SearchFilter;
import com.hodik.elastic.mappers.SortOrdersMapper;
import com.hodik.elastic.model.Project;
import com.hodik.elastic.repositories.ProjectSearchRepository;
import com.hodik.elastic.util.Operations;
import com.hodik.elastic.util.SearchColumn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ProjectSearchRepositoryImpl implements ProjectSearchRepository {
    private final ElasticsearchOperations elasticsearchOperations; // autowired bean
    private final SortOrdersMapper sortOrdersMapper;

    @Autowired
    public ProjectSearchRepositoryImpl(ElasticsearchOperations elasticsearchOperations, SortOrdersMapper sortOrdersMapper) {
        this.elasticsearchOperations = elasticsearchOperations;
        this.sortOrdersMapper = sortOrdersMapper;
    }

    @Override
    public Iterable<Project> findAllWithFilters(SearchCriteriaDto searchCriteriaDto) {
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
        CriteriaQuery criteriaQuery = new CriteriaQuery(criteria, pageable);
        SearchHits<Project> searchResponse = elasticsearchOperations.search(criteriaQuery,
                Project.class);
        return searchResponse.stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }

    private Pageable getPageable(SearchCriteriaDto searchCriteriaDto) {
        int page = searchCriteriaDto.getPage();
        int size = searchCriteriaDto.getSize();
        List<Sort.Order> orders = sortOrdersMapper.mapToSortOrder(searchCriteriaDto.getSorts());
        Sort sort = Sort.by(orders);
        Pageable pageable = PageRequest.of(page, size, sort);
        return pageable;
    }
}

