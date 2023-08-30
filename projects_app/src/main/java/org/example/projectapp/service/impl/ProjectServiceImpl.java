package org.example.projectapp.service.impl;

import org.example.projectapp.auth.AuthService;
import org.example.projectapp.controller.dto.ProjectDto;
import org.example.projectapp.controller.dto.ProjectInfoDto;
import org.example.projectapp.controller.dto.SearchDto;
import org.example.projectapp.mapper.PageableMapper;
import org.example.projectapp.mapper.ProjectMapper;
import org.example.projectapp.mapper.SearchProjectElasticCriteriaDtoMapper;
import org.example.projectapp.mapper.dto.ProjectElasticDto;
import org.example.projectapp.mapper.dto.SearchElasticCriteriaDto;
import org.example.projectapp.model.*;
import org.example.projectapp.repository.ProjectNotificationRepository;
import org.example.projectapp.repository.ProjectRepository;
import org.example.projectapp.restclient.ElasticProjectsServiceClient;
import org.example.projectapp.service.ProjectMemberService;
import org.example.projectapp.service.ProjectService;
import org.example.projectapp.service.dto.ProjectResponseDto;
import org.example.projectapp.service.exception.CustomEntityNotFoundException;
import org.example.projectapp.service.exception.ProjectAlreadyExistsException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectNotificationRepository projectNotificationRepository;
    private final AuthService authService;
    private final ProjectMemberService projectMemberService;
    private final SearchCriteriaBuilder<Project> searchCriteriaBuilder;
    private final ElasticProjectsServiceClient elasticProjectsServiceClient;
    private final ProjectMapper projectMapper;
    private final PageableMapper pageableMapper;
    private final SearchProjectElasticCriteriaDtoMapper elasticCriteriaDtoMapper;

    public ProjectServiceImpl(ProjectRepository projectRepository,
                              ProjectNotificationRepository projectNotificationRepository,
                              AuthService authService, ProjectMemberService projectMemberService,
                              SearchCriteriaBuilder<Project> searchCriteriaBuilder,
                              ElasticProjectsServiceClient elasticProjectsServiceClient,
                              ProjectMapper projectMapper, PageableMapper pageableMapper,
                              @Qualifier("searchProjectElasticCriteriaDtoMapper") SearchProjectElasticCriteriaDtoMapper elasticCriteriaDtoMapper) {
        this.projectRepository = projectRepository;
        this.projectNotificationRepository = projectNotificationRepository;
        this.authService = authService;
        this.projectMemberService = projectMemberService;
        this.searchCriteriaBuilder = searchCriteriaBuilder;
        this.elasticProjectsServiceClient = elasticProjectsServiceClient;
        this.projectMapper = projectMapper;
        this.pageableMapper = pageableMapper;
        this.elasticCriteriaDtoMapper = elasticCriteriaDtoMapper;
    }

    @Override
    @Transactional
    public Project createProject(ProjectDto projectDto) {
        String projectName = projectDto.getName();
        Project projectFromDb = projectRepository.findByName(projectName);
        if (projectFromDb != null) {
            throw new ProjectAlreadyExistsException("Project already exists", projectName);
        }
        Project project = buildProject(projectDto);
        User userFromAuth = authService.getUserFromAuth();
        projectRepository.saveAndFlush(project);
        projectMemberService.saveProjectMemberAndReturn(ProjectRole.OWNER, project, userFromAuth);
        ProjectElasticDto projectElasticDto = projectMapper.convertToProjectElasticDto(project);
        elasticProjectsServiceClient.createProject(projectElasticDto);
        return project;
    }

    @Override
    @Transactional
    public void enableNotification(Long projectId, boolean enable) {
        User userFromAuth = authService.getUserFromAuth();
        ProjectUserCompositeKey id = new ProjectUserCompositeKey(userFromAuth.getId(), projectId);
        Project project = tryGetProject(projectId);
        ProjectNotification projectNotification =
                projectNotificationRepository.findById(id).orElse(buildProjectNotification(id, userFromAuth, project));
        projectNotification.setNotificationEnabled(enable);
        projectNotificationRepository.save(projectNotification);
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#id,'org.example.projectapp.model.Project', 'projects:write')")
    public ProjectResponseDto updateProjectInfo(Long id, ProjectInfoDto dto) {
        Project project = tryGetProject(id);
        mergeProject(project, dto);
        ProjectElasticDto projectElasticDto = projectMapper.convertToProjectElasticDto(project);
        elasticProjectsServiceClient.updateProject(id, projectElasticDto);
        return saveAndReturnDto(project);
    }

    private ProjectResponseDto saveAndReturnDto(Project project) {
        Project savedProject = projectRepository.save(project);
        return projectMapper(savedProject);
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#id,'org.example.projectapp.model.Project', 'projects:write')")
    public ProjectResponseDto makeProjectPrivate(Long id, Boolean isPrivate) {
        Project project = tryGetProject(id);
        project.setPrivate(isPrivate);
        ProjectElasticDto projectElasticDto = projectMapper.convertToProjectElasticDto(project);
        elasticProjectsServiceClient.updateProject(id, projectElasticDto);
        return saveAndReturnDto(project);
    }

    @Override
    public Page<ProjectResponseDto> findProjectsByFilters(SearchDto searchDto) {
        Specification<Project> spec =
                searchCriteriaBuilder.buildSearchSpecificationWithPrivateProject(searchDto.getFilters());
        Pageable pageable = pageableMapper.getPageable(searchDto);
        Page<Project> projects = projectRepository.findAll(spec, pageable);

        List<ProjectResponseDto> collect = projects.stream().map(this::projectMapper).collect(Collectors.toList());
        return new PageImpl<>(collect);
    }

    @Override
    public List<ProjectResponseDto> findProjectsInElastic(SearchDto searchDto) {
        SearchElasticCriteriaDto searchElasticCriteriaDto =
                elasticCriteriaDtoMapper.convertToSearchElasticCriteriaDto(searchDto);
        return elasticProjectsServiceClient.searchProject(searchElasticCriteriaDto);
    }


    @Override
    public List<Project> findProjectsByListId(List<Long> ids) {
        return projectRepository.findAllById(ids);
    }

    @Override
    public List<Project> findAllProjects() {
        return projectRepository.findAll();
    }

    private ProjectResponseDto projectMapper(Project project) {
        return ProjectResponseDto.builder()
                .id(project.getId())
                .name(project.getName())
                .category(project.getCategory())
                .description(project.getDescription())
                .isCommercial(project.isCommercial())
                .isPrivate(project.isPrivate())
                .createdDate(project.getCreatedDate())
                .startDate(project.getStartDate())
                .scheduledEndDate(project.getScheduledEndDate())
                .status(project.getStatus())
                .build();
    }

    private void mergeProject(Project project, ProjectInfoDto dto) {
        String category = dto.getCategory();
        if (category != null) {
            project.setCategory(category);
        }
        String name = dto.getName();
        if (name != null) {
            project.setName(name);
        }

        ProjectStatus status = dto.getStatus();
        if (status != null) {
            project.setStatus(status);
        }
        Boolean isCommercial = dto.getIsCommercial();
        if (isCommercial != null) {
            project.setCommercial(isCommercial);
        }
        String description = dto.getDescription();
        if (description != null) {
            project.setDescription(description);
        }
        project.setStartDate(dto.getStartDate());
        project.setScheduledEndDate(dto.getScheduledEndDate());
    }

    private ProjectNotification buildProjectNotification(ProjectUserCompositeKey id, User userFromAuth, Project project) {
        return ProjectNotification.builder()
                .id(id)
                .project(project)
                .user(userFromAuth)
                .build();
    }

    private Project tryGetProject(Long projectId) {
        try {
            return projectRepository.getOne(projectId);
        } catch (EntityNotFoundException e) {
            throw new CustomEntityNotFoundException(projectId, Project.class.getSimpleName());
        }
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
                .createdDate(LocalDateTime.now())
                .scheduledEndDate(projectDto.getScheduledEndDate())
                .startDate(projectDto.getStartDate())
                .status(projectStatus == null ? ProjectStatus.NEW : projectStatus)
                .description(projectDto.getDescription())
                .build();
    }
}
