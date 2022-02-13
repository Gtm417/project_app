package org.example.projectapp.repository;

import org.example.projectapp.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface ProjectRepository extends JpaRepository<Project, Long> {
    Project findByName(String name);

}
