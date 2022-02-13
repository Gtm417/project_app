package org.example.projectapp.model;


import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static org.example.projectapp.model.ProjectPermission.*;

public enum ProjectRole {
    TRAINEE(ONLY_READ()),
    DEVELOPER(ONLY_READ()),
    RECRUITER(concatPermissions(ONLY_READ(), VACANCY_WRITE)),
    MANAGER(concatPermissions(ONLY_READ(), VACANCY_WRITE)),
    ADMIN(concatPermissions(ONLY_READ(), VACANCY_WRITE, PROJECT_WRITE)),
    OWNER(Arrays.stream(ProjectPermission.values()).collect(Collectors.toSet()));

    private final Set<ProjectPermission> projectPermissions;

    ProjectRole(Set<ProjectPermission> projectPermissions) {
        this.projectPermissions = projectPermissions;
    }

    public Set<ProjectPermission> getPermissions() {
        return projectPermissions;
    }

    private static Set<ProjectPermission> concatPermissions(Set<ProjectPermission> permissions, ProjectPermission... permissionsToAdd) {
        permissions.addAll(Arrays.stream(permissionsToAdd).collect(Collectors.toSet()));
        return permissions;
    }
}
