package org.example.projectapp.auth.controller.dto;

import lombok.Data;

@Data
public class AuthenticationRequestDTO {
    private String email;
    private String password;
}
