package com.hodik.elastic.repository;

import com.hodik.elastic.dto.SearchCriteriaDto;
import com.hodik.elastic.model.Vacancy;
import io.micrometer.core.annotation.Timed;

import java.util.List;


@Timed("elastic.vacancy.search")
public interface VacancySearchRepository {
   List<Vacancy> findAllWithFilters (SearchCriteriaDto searchCriteriaDto);
}
