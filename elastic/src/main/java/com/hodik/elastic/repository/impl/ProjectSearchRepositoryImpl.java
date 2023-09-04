package com.hodik.elastic.repository.impl;


import com.hodik.elastic.dto.SearchCriteriaDto;
import com.hodik.elastic.model.Project;
import com.hodik.elastic.repository.ProjectSearchRepository;
import com.hodik.elastic.repository.search.builder.EsQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ProjectSearchRepositoryImpl implements ProjectSearchRepository {
    private final ElasticsearchOperations elasticsearchOperations; // autowired bean

    private final EsQueryBuilder queryBuilder;


    @Autowired
    public ProjectSearchRepositoryImpl(ElasticsearchOperations elasticsearchOperations, EsQueryBuilder queryBuilder) {
        this.elasticsearchOperations = elasticsearchOperations;

        this.queryBuilder = queryBuilder;
    }

    @Override
    public List<Project> findAllWithFilters(SearchCriteriaDto searchCriteriaDto) {

        CriteriaQuery criteriaQuery = queryBuilder.getCriteriaQuery(searchCriteriaDto);
        SearchHits<Project> searchResponse = elasticsearchOperations.search(criteriaQuery,
                Project.class);

        return searchResponse.stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }
}

