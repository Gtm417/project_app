package com.hodik.elastic.services;

import com.hodik.elastic.model.User;
import com.hodik.elastic.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EsUserService {
    private final UserRepository userRepository;
@Autowired
    public EsUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void save(User user) {
    }

    public void update(User user) {
    }

    public void delete(long id) {
    }

    public Iterable<User> findAll() {
       return userRepository.findAll();
    }
}

