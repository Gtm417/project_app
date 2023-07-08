package com.hodik.elastic.repositories;

import com.hodik.elastic.model.Vacancy;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VacancyRepository extends ElasticsearchRepository<Vacancy, String> {

    Optional<Vacancy> findById (long id);

}
