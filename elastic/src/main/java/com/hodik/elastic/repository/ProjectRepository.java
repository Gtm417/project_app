package com.hodik.elastic.repository;

import com.hodik.elastic.model.Project;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.Optional;


public interface ProjectRepository extends ElasticsearchRepository<Project, String> {


    void deleteById(Long id);

    Optional<Project> findById(Long id);


}
