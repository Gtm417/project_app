package com.hodik.elastic.repositories;

import com.hodik.elastic.model.Project;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectRepository extends ElasticsearchRepository<Project, String> {


    void deleteById(Long id);


    Optional<Project> findById(Long id);

    Iterable<Project> findAll(Criteria criteria, Pageable pageable);

}
