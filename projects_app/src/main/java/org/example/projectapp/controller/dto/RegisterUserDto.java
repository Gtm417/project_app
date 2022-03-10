package org.example.projectapp.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.projectapp.model.Role;
import org.example.projectapp.model.UserType;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class RegisterUserDto {
    @NotBlank(message = "Should not be empty")
    private String firstName;
    @NotBlank(message = "Should not be empty")
    private String lastName;
    @NotBlank(message = "Should not be empty")
    @Email(message="Should match email pattern")
    private String email;
    @NotBlank(message = "Should not be empty")
    private String password;
    @NotNull(message = "Should not be empty")
    private UserType type;
    private Role role;
}
