package org.example.projectapp.service.impl;

import org.example.projectapp.controller.dto.ProjectMemberDto;
import org.example.projectapp.model.Project;
import org.example.projectapp.model.ProjectMember;
import org.example.projectapp.model.ProjectRole;
import org.example.projectapp.model.User;
import org.example.projectapp.repository.ProjectMemberRepository;
import org.example.projectapp.repository.ProjectRepository;
import org.example.projectapp.repository.UserRepository;
import org.example.projectapp.service.ProjectMemberService;
import org.example.projectapp.service.dto.ProjectMemberResponseDto;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ProjectMemberServiceImpl implements ProjectMemberService {
    private final ProjectMemberRepository projectMemberRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    public ProjectMemberServiceImpl(ProjectMemberRepository projectMemberRepository,
                                    UserRepository userRepository, ProjectRepository projectRepository) {
        this.projectMemberRepository = projectMemberRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
    }

    @Override
    public ProjectRole getAllProjectRolesForUser(Project project, User user) {
        return projectMemberRepository.getProjectRoleForUser(project, user);
    }

    @Override
    public ProjectRole getAllProjectRolesForUser(Long projectId, User user) {
        return projectMemberRepository.getProjectRoleForUser(projectId, user);
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#projectId,'org.example.projectapp.model.Project', 'members:write')")
    public ProjectMemberResponseDto addMemberToProject(Long projectId, ProjectMemberDto dto) {
        User user = userRepository.getOne(dto.getUserId());
        Project project = projectRepository.getOne(projectId);
        ProjectMember projectMember = projectMemberRepository.getByProjectAndUser(project, user);
        if (projectMember != null) {
            projectMember.setProjectRole(dto.getRole());
            return mapToProjectMemberResponseDto(projectMember);
        }

        return saveProjectMemberAndReturn(dto.getRole(), project, user);
    }

    public ProjectMemberResponseDto saveProjectMemberAndReturn(ProjectRole role, Project project, User userFromAuth) {
        ProjectMember projectMember = ProjectMember.builder()
                .project(project)
                .user(userFromAuth)
                .addedAt(LocalDateTime.now())
                .projectRole(role)
                .build();
        ProjectMember savedProjectMember = projectMemberRepository.save(projectMember);
        return mapToProjectMemberResponseDto(savedProjectMember);
    }

    private ProjectMemberResponseDto mapToProjectMemberResponseDto(ProjectMember savedProjectMember) {
        return ProjectMemberResponseDto.builder()
                .id(savedProjectMember.getId())
                .projectId(savedProjectMember.getProject().getId())
                .userId(savedProjectMember.getUser().getId())
                .addedAt(savedProjectMember.getAddedAt())
                .projectRole(savedProjectMember.getProjectRole())
                .build();
    }
}
