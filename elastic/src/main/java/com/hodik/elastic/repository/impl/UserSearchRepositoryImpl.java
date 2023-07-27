package com.hodik.elastic.repository.impl;

import com.hodik.elastic.dto.SearchCriteriaDto;
import com.hodik.elastic.model.User;
import com.hodik.elastic.repository.UserSearchRepository;
import com.hodik.elastic.repository.search.builder.EsQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;
@Repository
public class UserSearchRepositoryImpl implements UserSearchRepository {
    private final ElasticsearchOperations elasticsearchOperations;
    private final EsQueryBuilder queryBuilder;

    @Autowired
    public UserSearchRepositoryImpl(ElasticsearchOperations elasticsearchOperations, EsQueryBuilder queryBuilder) {
        this.elasticsearchOperations = elasticsearchOperations;
        this.queryBuilder = queryBuilder;
    }

    @Override
    public List<User> findAllWithFilters(SearchCriteriaDto searchCriteriaDto) {
        SearchHits<User> searchResponse =
                elasticsearchOperations.search(
                        queryBuilder.getCriteriaQuery(searchCriteriaDto), User.class);
        return searchResponse.stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }
}
