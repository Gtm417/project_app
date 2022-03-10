package org.example.projectapp.model;

import java.util.Arrays;
import java.util.Set;

public enum ProjectPermission {
    MEMBER_READ("members:read"),
    MEMBER_WRITE("members:write"),
    VACANCY_READ("vacancies:read"),
    VACANCY_WRITE("vacancies:write"),
    PROJECT_READ("projects:read"),
    PROJECT_WRITE("projects:write");

    private final String name;

    ProjectPermission(String name) {
        this.name = name;
    }

    public static ProjectPermission getPermissionFromName(String name) {
        return Arrays.stream(ProjectPermission.values())
                .filter(projectPermission -> projectPermission.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Permission name is incorrect"));
    }

    public static Set<ProjectPermission> ONLY_READ() {
        return Set.of(MEMBER_READ, VACANCY_READ, PROJECT_READ);
    }


    public String getName() {
        return name;
    }

}
