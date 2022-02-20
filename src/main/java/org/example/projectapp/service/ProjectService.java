package org.example.projectapp.service;

import org.example.projectapp.controller.dto.ProjectDto;
import org.example.projectapp.controller.dto.ProjectInfoDto;
import org.example.projectapp.model.Project;
import org.example.projectapp.service.dto.ProjectResponseDto;

public interface ProjectService {
    Project createProject(ProjectDto projectDto);

    void enableNotification(Long projectId, boolean enable);

    ProjectResponseDto updateProjectInfo(Long id, ProjectInfoDto dto);

    ProjectResponseDto makeProjectPrivate(Long id, Boolean isPrivate);
}
