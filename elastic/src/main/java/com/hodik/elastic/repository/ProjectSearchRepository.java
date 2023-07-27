package com.hodik.elastic.repository;

import com.hodik.elastic.dto.SearchCriteriaDto;
import com.hodik.elastic.model.Project;

import java.util.List;


public interface ProjectSearchRepository {

    List<Project> findAllWithFilters(SearchCriteriaDto searchCriteriaDto);
}

