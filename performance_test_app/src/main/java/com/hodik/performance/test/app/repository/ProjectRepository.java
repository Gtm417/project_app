package com.hodik.performance.test.app.repository;

import com.hodik.performance.test.app.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query(value = "SELECT * FROM projects ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Project findRandomEntity();
}

