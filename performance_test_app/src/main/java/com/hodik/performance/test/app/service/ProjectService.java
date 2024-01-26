package com.hodik.performance.test.app.service;

import com.hodik.performance.test.app.model.Project;
import com.hodik.performance.test.app.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    private ProjectRepository repository;

    @Autowired
    public ProjectService(ProjectRepository repository) {
        this.repository = repository;
    }


    public void saveProjectsList(List<Project> projectList) {
        repository.saveAll(projectList);
    }

    public Project findRandomProject() {

        return repository.getReferenceById(1L);
    }
}

