package org.example.projectapp.service.exception;

public class ProjectNotMatchException extends RuntimeException {
    public ProjectNotMatchException() {
        super();
    }

    public ProjectNotMatchException(String message) {
        super(message);
    }
}
