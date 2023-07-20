package com.hodik.elastic.repositories;

import com.hodik.elastic.dto.SearchCriteriaDto;
import com.hodik.elastic.model.Vacancy;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VacancySearchRepository {
   List<Vacancy> findAllWithFilters (SearchCriteriaDto searchCriteriaDto);
}
