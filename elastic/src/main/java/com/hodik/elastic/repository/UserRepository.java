package com.hodik.elastic.repository;

import com.hodik.elastic.model.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.Optional;


public interface UserRepository extends ElasticsearchRepository<User, String> {
    void deleteById(long id);

    Optional<User> findById(long id);
}
