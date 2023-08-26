package org.example.projectapp.service;

import org.example.projectapp.controller.dto.ProjectDto;
import org.example.projectapp.controller.dto.ProjectInfoDto;
import org.example.projectapp.controller.dto.SearchDto;
import org.example.projectapp.model.Project;
import org.example.projectapp.service.dto.ProjectResponseDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProjectService {
    Project createProject(ProjectDto projectDto);

    void enableNotification(Long projectId, boolean enable);

    ProjectResponseDto updateProjectInfo(Long id, ProjectInfoDto dto);

    ProjectResponseDto makeProjectPrivate(Long id, Boolean isPrivate);

    Page<ProjectResponseDto> findProjectsByFilters(SearchDto searchDto);

    List<ProjectResponseDto> findProjectsInElastic(SearchDto searchDto);

    List<Project> findProjectsByListId(List<Long> ids);

    List<Project> findAllProjects();

}
