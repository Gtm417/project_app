package org.example.projectapp.model;


import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static org.example.projectapp.model.ProjectPermission.*;

public enum ProjectRole {
    TRAINEE(ONLY_READ()),
    DEVELOPER(ONLY_READ()),
    RECRUITER(Set.of(MEMBER_READ, PROJECT_READ, VACANCY_READ, VACANCY_WRITE)),
    MANAGER(Set.of(MEMBER_READ, PROJECT_READ, VACANCY_READ, VACANCY_WRITE)),
    ADMIN(Set.of(MEMBER_READ, PROJECT_READ, VACANCY_READ, VACANCY_WRITE, PROJECT_WRITE)),
    OWNER(Arrays.stream(ProjectPermission.values()).collect(Collectors.toSet()));

    private Set<ProjectPermission> projectPermissions;

    ProjectRole() {
        //needed for jpa
    }

    ProjectRole(Set<ProjectPermission> projectPermissions) {
        this.projectPermissions = projectPermissions;
    }

    public Set<ProjectPermission> getPermissions() {
        return projectPermissions;
    }
}
