package org.example.projectapp.service;

import org.example.projectapp.model.Project;
import org.example.projectapp.model.ProjectRole;
import org.example.projectapp.model.User;

import java.util.List;

public interface ProjectMemberService {
    List<ProjectRole> getAllProjectRolesForUser(Project project, User user);

    List<ProjectRole> getAllProjectRolesForUser(Long projectId, User user);
}
