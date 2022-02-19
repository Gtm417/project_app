package org.example.projectapp.service;

import org.example.projectapp.controller.dto.ProjectDto;
import org.example.projectapp.model.Project;

public interface ProjectService {
    Project createProject(ProjectDto projectDto);

    void enableNotification(Long projectId, boolean enable);
}
