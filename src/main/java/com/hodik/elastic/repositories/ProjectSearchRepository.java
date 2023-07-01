package com.hodik.elastic.repositories;

import com.hodik.elastic.dto.SearchCriteriaDto;
import com.hodik.elastic.model.Project;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectSearchRepository {

    Iterable<Project> findAllWithFilters(SearchCriteriaDto searchCriteriaDto);
}

