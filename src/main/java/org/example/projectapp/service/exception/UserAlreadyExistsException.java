package org.example.projectapp.service.exception;

import org.example.projectapp.controller.dto.RegisterUserDto;

public class UserAlreadyExistsException extends RuntimeException {
    private final RegisterUserDto registerDto;
    public UserAlreadyExistsException(RegisterUserDto registerDto, String message) {
        super(message);
        this.registerDto = registerDto;
    }

    public RegisterUserDto getRegisterDto() {
        return registerDto;
    }
}
