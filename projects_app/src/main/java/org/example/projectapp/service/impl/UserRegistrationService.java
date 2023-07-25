package org.example.projectapp.service.impl;

import org.example.projectapp.controller.dto.RegisterUserDto;
import org.example.projectapp.mappers.UserMapper;
import org.example.projectapp.model.Status;
import org.example.projectapp.model.User;
import org.example.projectapp.repository.UserRepository;
import org.example.projectapp.restclient.ElasticUsersServiceClient;
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
    private final ElasticUsersServiceClient elasticUsersServiceClient;
    private final UserMapper userMapper;

    @Autowired
    public UserRegistrationService(PasswordEncoder passwordEncoder, UserRepository userRepository, ElasticUsersServiceClient elasticUsersServiceClient, UserMapper userMapper) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.elasticUsersServiceClient = elasticUsersServiceClient;
        this.userMapper = userMapper;
    }

    @Override
    public User register(RegisterUserDto registerDto) {
        userRepository.findByEmail(registerDto.getEmail())
                .ifPresent((u) -> {
                    throw new UserAlreadyExistsException(registerDto, "User already exists");
                });

        User user = buildUser(registerDto);
        User savedUser = userRepository.save(user);
        elasticUsersServiceClient.createUser(userMapper.convertToUserElasticDto(user));
        return savedUser;
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
