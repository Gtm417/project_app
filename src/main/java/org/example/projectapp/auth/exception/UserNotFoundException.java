package org.example.projectapp.auth.exception;

public class UserNotFoundException extends RuntimeException {
    private String email;
    public UserNotFoundException(String message, String email) {
        super(message);
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
