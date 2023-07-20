package com.hodik.elastic.repositories.impl;

import com.hodik.elastic.dto.SearchCriteriaDto;
import com.hodik.elastic.model.Vacancy;
import com.hodik.elastic.repositories.VacancySearchRepository;
import com.hodik.elastic.repositories.search.builder.EsQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;
@Repository
public class VacancySearchRepositoryImpl implements VacancySearchRepository {
    private final ElasticsearchOperations elasticsearchOperations;
    private final EsQueryBuilder queryBuilder;

    @Autowired
    public VacancySearchRepositoryImpl(ElasticsearchOperations elasticsearchOperations, EsQueryBuilder queryBuilder) {
        this.elasticsearchOperations = elasticsearchOperations;

        this.queryBuilder = queryBuilder;
    }

    @Override
    public List<Vacancy> findAllWithFilters(SearchCriteriaDto searchCriteriaDto) {
        SearchHits<Vacancy> searchResponse =
                elasticsearchOperations.search(
                        queryBuilder.getCriteriaQuery(searchCriteriaDto), Vacancy.class);
        return searchResponse.stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }
}
