package org.example.projectapp.service.impl;

import org.example.projectapp.controller.dto.RegisterUserDto;
import org.example.projectapp.model.Status;
import org.example.projectapp.model.User;
import org.example.projectapp.repository.UserRepository;
import org.example.projectapp.service.RegistrationService;
import org.example.projectapp.service.exception.UserAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static org.example.projectapp.model.Role.ROLE_ADMIN;
import static org.example.projectapp.model.Role.ROLE_USER;

@Service
public class UserRegistrationService implements RegistrationService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Autowired
    public UserRegistrationService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public User register(RegisterUserDto registerDto) {
        userRepository.findByEmail(registerDto.getEmail())
                .ifPresent((u) -> {throw new UserAlreadyExistsException(registerDto, "User already exists");});

        User user = buildUser(registerDto);
        return userRepository.save(user);
    }

    private User buildUser(RegisterUserDto dto) {
        return User.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .status(Status.NEW)
                .type(dto.getType())
                .role(dto.getRole() == null ? ROLE_USER : ROLE_ADMIN)
                .build();
    }
}
