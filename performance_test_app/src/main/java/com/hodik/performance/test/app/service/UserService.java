package com.hodik.performance.test.app.service;

import com.hodik.performance.test.app.model.User;
import com.hodik.performance.test.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public void saveUsersList(List<User> users) {
        repository.saveAll(users);
    }
}
