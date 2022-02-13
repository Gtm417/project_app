package org.example.projectapp.service.impl;

import org.example.projectapp.model.Project;
import org.example.projectapp.model.ProjectRole;
import org.example.projectapp.model.User;
import org.example.projectapp.repository.ProjectMemberRepository;
import org.example.projectapp.service.ProjectMemberService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectMemberServiceImpl implements ProjectMemberService {
    private final ProjectMemberRepository projectMemberRepository;

    public ProjectMemberServiceImpl(ProjectMemberRepository projectMemberRepository) {
        this.projectMemberRepository = projectMemberRepository;
    }

    @Override
    public List<ProjectRole> getAllProjectRolesForUser(Project project, User user) {
        return projectMemberRepository.getAllProjectRolesForUser(project, user);
    }

    @Override
    public List<ProjectRole> getAllProjectRolesForUser(Long projectId, User user) {
        return projectMemberRepository.getAllProjectRolesForUser(projectId, user);
    }
}
