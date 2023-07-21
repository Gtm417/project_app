package com.hodik.elastic.repositories.impl;

import com.google.gson.Gson;
import com.hodik.elastic.dto.SearchCriteriaDto;
import com.hodik.elastic.model.User;
import com.hodik.elastic.repositories.search.builder.EsQueryBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ResourceUtil;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserSearchRepositoryImplTest {
    private final Gson gson= new Gson();
    private final SearchCriteriaDto searchCriteriaDto = gson.fromJson(ResourceUtil.readFileFromClasspath("search.criteria.user.success.json"), SearchCriteriaDto.class);
    @Mock
    private ElasticsearchOperations elasticsearchOperations; // autowired bean
    @Mock
    private EsQueryBuilder queryBuilder;
    @Mock
    private CriteriaQuery criteriaQuery;
    @Mock
    private SearchHits<User> searchHits;
    @InjectMocks
    private UserSearchRepositoryImpl searchRepository;
    @Test
    void findAllWithFilters() {

        //given

        when(queryBuilder.getCriteriaQuery(searchCriteriaDto)).thenReturn(criteriaQuery);

        when(elasticsearchOperations.search(criteriaQuery, User.class)).thenReturn(searchHits);

        //when
        List<User> users = searchRepository.findAllWithFilters(searchCriteriaDto);

        //then
        assertNotNull(users);

    }
}