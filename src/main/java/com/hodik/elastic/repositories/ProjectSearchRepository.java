package com.hodik.elastic.repositories;

import com.hodik.elastic.dto.SearchCriteriaDto;
import com.hodik.elastic.model.Project;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectSearchRepository {

    List<Project> findAllWithFilters(SearchCriteriaDto searchCriteriaDto);
}

