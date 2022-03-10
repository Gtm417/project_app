package org.example.projectapp.auth;

import org.example.projectapp.model.User;
import org.springframework.security.core.Authentication;

public interface AuthService {
    Authentication getAuthentication(String token);

    User getUserFromAuth();

    User getUserFromAuth(Authentication authentication);

    String getUserEmail();

}
