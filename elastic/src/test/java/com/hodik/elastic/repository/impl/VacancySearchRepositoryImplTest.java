package com.hodik.elastic.repository.impl;

import com.google.gson.Gson;
import com.hodik.elastic.dto.SearchCriteriaDto;
import com.hodik.elastic.model.Vacancy;
import com.hodik.elastic.repository.search.builder.EsQueryBuilder;
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
class VacancySearchRepositoryImplTest {
    private final Gson gson= new Gson();
    private final SearchCriteriaDto searchCriteriaDto = gson.fromJson(ResourceUtil.readFileFromClasspath("search.criteria.vacancy.success.json"), SearchCriteriaDto.class);
    @Mock
    private ElasticsearchOperations elasticsearchOperations; // autowired bean
    @Mock
    private EsQueryBuilder queryBuilder;
    @Mock
    private CriteriaQuery criteriaQuery;
    @Mock
    private SearchHits<Vacancy> searchHits;
    @InjectMocks
    private VacancySearchRepositoryImpl searchRepository;
    @Test
    void findAllWithFilters() {
        //given

        when(queryBuilder.getCriteriaQuery(searchCriteriaDto)).thenReturn(criteriaQuery);

        when(elasticsearchOperations.search(criteriaQuery, Vacancy.class)).thenReturn(searchHits);

        //when
        List<Vacancy> vacancies = searchRepository.findAllWithFilters(searchCriteriaDto);

        //then
        assertNotNull(vacancies);
    }
}