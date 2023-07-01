package com.hodik.elastic.repositories;

import com.hodik.elastic.dto.SearchCriteriaDto;
import com.hodik.elastic.model.User;

public interface UserSearchRepository {
    Iterable<User> findAllWithFilters (SearchCriteriaDto searchCriteriaDto);
}
