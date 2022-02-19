package org.example.projectapp.service.exception;

public class ProjectAlreadyExistsException extends RuntimeException {
    private final String projectName;

    public ProjectAlreadyExistsException(String message, String projectName) {
        super(message);
        this.projectName = projectName;
    }

    public String getProjectName() {
        return projectName;
    }
}
