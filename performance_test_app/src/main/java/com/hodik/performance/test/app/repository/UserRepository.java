package com.hodik.performance.test.app.repository;

import com.hodik.performance.test.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
