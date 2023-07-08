package com.hodik.elastic.repositories;

import com.hodik.elastic.model.User;
import org.elasticsearch.xcontent.ObjectParser;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends ElasticsearchRepository<User, String> {
    void deleteById(long id);

    Optional<User> findById(long id);
}
