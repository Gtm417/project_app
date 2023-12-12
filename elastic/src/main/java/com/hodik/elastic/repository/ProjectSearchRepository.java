package com.hodik.elastic.repository;

import com.hodik.elastic.dto.SearchCriteriaDto;
import com.hodik.elastic.model.Project;
import io.micrometer.core.annotation.Timed;

import java.util.List;


public interface ProjectSearchRepository {
    @Timed("elastic.projects.search")
    List<Project> findAllWithFilters(SearchCriteriaDto searchCriteriaDto);
}

