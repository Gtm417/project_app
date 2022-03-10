package org.example.projectapp.service;

import org.example.projectapp.controller.dto.RegisterUserDto;
import org.example.projectapp.model.User;

public interface RegistrationService {
    User register(RegisterUserDto registerDto);
}
