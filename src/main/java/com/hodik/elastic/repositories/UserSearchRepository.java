package com.hodik.elastic.repositories;

import com.hodik.elastic.dto.SearchCriteriaDto;
import com.hodik.elastic.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserSearchRepository {
    List<User> findAllWithFilters (SearchCriteriaDto searchCriteriaDto);
}
