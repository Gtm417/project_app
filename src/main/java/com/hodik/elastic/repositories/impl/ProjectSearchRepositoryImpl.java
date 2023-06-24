package com.hodik.elastic.repositories.impl;


import com.hodik.elastic.dto.SearchCriteriaDto;
import com.hodik.elastic.dto.SearchFilter;
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
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ProjectSearchRepositoryImpl implements ProjectSearchRepository {
    private final ElasticsearchOperations elasticsearchOperations; // autowired bean

    @Autowired
    public ProjectSearchRepositoryImpl(ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
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

        int page = searchCriteriaDto.getPage();
        int size = searchCriteriaDto.getSize();
//        Sort.Direction direction;
//        if (searchCriteriaDto.getSorts().get(0).getAscending()) {
//            direction = Sort.Direction.ASC;
//        } else {
//            direction = Sort.Direction.DESC;
//        }
//        List<SearchColumn> columns = searchCriteriaDto.getSorts().stream().map(x -> x.getColumn()).collect(Collectors.toList());
//        Sort sort =Sort.by(direction, String.valueOf(columns));
//        Pageable pageable = PageRequest.of(page, size, sort);
        Pageable pageable = PageRequest.of(page, size);


       CriteriaQuery criteriaQuery = new CriteriaQuery(criteria, pageable);
//        CriteriaQuery criteriaQuery = new CriteriaQuery(criteria);

        return elasticsearchOperations.search(criteriaQuery,

                        Project.class).stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }
}

//        public void search() {
//            Criteria criteria = new Criteria();
//            criteria.and(new Criteria("foo").is(foo));
//            criteria.
//            criteria.and(new Criteria("bar").in(bars));
//            CriteriaQuery criteriaQuery = new CriteriaQuery(criteria);
//            elasticsearchOperations.search(criteriaQuery,
//                            FooElasticEntity.class).stream()
//                    .map(SearchHit::getContent)
//                    .collect(Collectors.toList())
//        }