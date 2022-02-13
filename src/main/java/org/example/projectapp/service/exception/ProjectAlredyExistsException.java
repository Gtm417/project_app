package org.example.projectapp.service.exception;

public class ProjectAlredyExistsException extends RuntimeException {
    private final String projectName;
    public ProjectAlredyExistsException(String message, String projectName) {
        super(message);
        this.projectName = projectName;
    }

    public String getProjectName() {
        return projectName;
    }
}
