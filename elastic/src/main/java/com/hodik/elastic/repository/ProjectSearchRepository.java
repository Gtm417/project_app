package com.hodik.elastic.repository;

import com.hodik.elastic.dto.SearchCriteriaDto;
import com.hodik.elastic.model.Project;
import io.micrometer.core.annotation.Timed;

import java.util.List;

@Timed("elastic.projects.search")
public interface ProjectSearchRepository {
    List<Project> findAllWithFilters(SearchCriteriaDto searchCriteriaDto);
}

