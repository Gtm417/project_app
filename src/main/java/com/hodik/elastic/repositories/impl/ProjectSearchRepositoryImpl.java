package com.hodik.elastic.repositories.impl;


import com.hodik.elastic.dto.SearchCriteriaDto;
import com.hodik.elastic.mappers.SortOrdersMapper;
import com.hodik.elastic.model.Project;
import com.hodik.elastic.repositories.ProjectSearchRepository;
import com.hodik.elastic.repositories.search.builder.EsQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Repository;

import java.util.stream.Collectors;

@Repository
public class ProjectSearchRepositoryImpl implements ProjectSearchRepository {
    private final ElasticsearchOperations elasticsearchOperations; // autowired bean
    private final SortOrdersMapper sortOrdersMapper;
    private final EsQueryBuilder queryBuilder;

    @Autowired
    public ProjectSearchRepositoryImpl(ElasticsearchOperations elasticsearchOperations, SortOrdersMapper sortOrdersMapper, EsQueryBuilder queryBuilder) {
        this.elasticsearchOperations = elasticsearchOperations;
        this.sortOrdersMapper = sortOrdersMapper;
        this.queryBuilder = queryBuilder;
    }

    @Override
    public Iterable<Project> findAllWithFilters(SearchCriteriaDto searchCriteriaDto) {
        SearchHits<Project> searchResponse = elasticsearchOperations.search(queryBuilder.getCriteriaQuery(searchCriteriaDto),
                Project.class);
        return searchResponse.stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }


}

