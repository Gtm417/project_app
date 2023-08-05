package org.example.projectapp.service.exception.errorResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.projectapp.controller.dto.RegisterUserDto;

@Data
@AllArgsConstructor
public class RegisterUserDtoErrorResponse {
    private String message;
    private RegisterUserDto registerDto;

}
