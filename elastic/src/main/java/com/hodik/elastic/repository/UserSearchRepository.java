package com.hodik.elastic.repository;

import com.hodik.elastic.dto.SearchCriteriaDto;
import com.hodik.elastic.model.User;

import java.util.List;


public interface UserSearchRepository {
    List<User> findAllWithFilters (SearchCriteriaDto searchCriteriaDto);
}
