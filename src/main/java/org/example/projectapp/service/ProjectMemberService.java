package org.example.projectapp.service;

import org.example.projectapp.controller.dto.ProjectMemberDto;
import org.example.projectapp.model.Project;
import org.example.projectapp.model.ProjectRole;
import org.example.projectapp.model.User;
import org.example.projectapp.service.dto.ProjectMemberResponseDto;

public interface ProjectMemberService {
    ProjectRole getAllProjectRolesForUser(Project project, User user);

    ProjectRole getAllProjectRolesForUser(Long projectId, User user);

    ProjectMemberResponseDto addMemberToProject(Long projectId, ProjectMemberDto dto);

    ProjectMemberResponseDto saveProjectMemberAndReturn(ProjectRole role, Project project, User userFromAuth);
}
