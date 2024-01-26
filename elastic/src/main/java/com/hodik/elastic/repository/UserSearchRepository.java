package com.hodik.elastic.repository;

import com.hodik.elastic.dto.SearchCriteriaDto;
import com.hodik.elastic.model.User;
import io.micrometer.core.annotation.Timed;

import java.util.List;

@Timed("elastic.users.search")
public interface UserSearchRepository {
    List<User> findAllWithFilters (SearchCriteriaDto searchCriteriaDto);
}
