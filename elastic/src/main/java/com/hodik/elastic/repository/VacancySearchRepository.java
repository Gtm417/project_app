package com.hodik.elastic.repository;

import com.hodik.elastic.dto.SearchCriteriaDto;
import com.hodik.elastic.model.Vacancy;

import java.util.List;


public interface VacancySearchRepository {
   List<Vacancy> findAllWithFilters (SearchCriteriaDto searchCriteriaDto);
}
