package org.example.projectapp.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.example.projectapp.auth.AuthService;
import org.example.projectapp.controller.dto.ProjectDto;
import org.example.projectapp.model.*;
import org.example.projectapp.repository.ProjectMemberRepository;
import org.example.projectapp.repository.ProjectRepository;
import org.example.projectapp.service.ProjectService;
import org.example.projectapp.service.exception.ProjectAlredyExistsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final AuthService authService;

    public ProjectServiceImpl(ProjectRepository projectRepository,
                              ProjectMemberRepository projectMemberRepository, AuthService authService) {
        this.projectRepository = projectRepository;
        this.projectMemberRepository = projectMemberRepository;
        this.authService = authService;
    }

    @Override
    @Transactional
    public Project createProject(ProjectDto projectDto) {
        String projectName = projectDto.getName();
        Project projectFromDb = projectRepository.findByName(projectName);
        if (projectFromDb != null){
            throw new ProjectAlredyExistsException("Project already exists", projectName);
        }
        Project project = buildProject(projectDto);
        User userFromAuth = authService.getUserFromAuth();
        projectRepository.saveAndFlush(project);
        ProjectMember projectMember = buildProjectMember(project, userFromAuth);
        projectMemberRepository.save(projectMember);
        //todo check if I need to save projectMember instance
        return project;
    }

    private void setMemberOfProjects(User userFromAuth, ProjectMember projectMember) {
        Set<ProjectMember> resultSet = new HashSet<>();
        if (userFromAuth.getMemberOfProjects() == null) {
            resultSet.add(projectMember);
        }
        resultSet.add(projectMember);
        userFromAuth.setMemberOfProjects(resultSet);
    }

    private ProjectMember buildProjectMember(Project project, User user) {
        return ProjectMember.builder()
                .project(project)
                .user(user)
                .projectRole(ProjectRole.OWNER)
                .addedAt(LocalDateTime.now())
                .build();
    }

    private Project buildProject(ProjectDto projectDto) {
        ProjectStatus projectStatus = projectDto.getStatus();
        return Project.builder()
                .name(projectDto.getName())
                .category(projectDto.getCategory())
                .isPrivate(projectDto.getIsPrivate())
                .isCommercial(projectDto.getIsCommercial())
                .createDate(LocalDateTime.now())
                .scheduledEndDate(projectDto.getScheduledEndDate())
                .startDate(projectDto.getStartDate())
                .status(projectStatus == null ? ProjectStatus.NEW : projectStatus)
                .description(StringUtils.EMPTY)
                .build();
    }
}
