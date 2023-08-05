package com.hodik.elastic.repository;

import com.hodik.elastic.model.Vacancy;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.Optional;


public interface VacancyRepository extends ElasticsearchRepository<Vacancy, String> {

    Optional<Vacancy> findById (long id);
    void deleteById(long id);

}
