package org.example.projectapp.service.exception;

public class ProjectNotFoundException extends RuntimeException {
    private final Long projectId;

    public ProjectNotFoundException(String message, long projectId) {
        super(message);
        this.projectId = projectId;
    }

    public Long getProjectId() {
        return projectId;
    }
}
