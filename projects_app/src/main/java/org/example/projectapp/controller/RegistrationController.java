package org.example.projectapp.controller;


import org.example.projectapp.controller.dto.RegisterUserDto;
import org.example.projectapp.model.User;
import org.example.projectapp.service.RegistrationService;
import org.example.projectapp.service.exception.UserAlreadyExistsException;
import org.example.projectapp.service.exception.errorResponse.RegisterUserDtoErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/registration")
public class RegistrationController {
    Logger logger = LoggerFactory.getLogger(RegistrationController.class);

    private final RegistrationService registrationService;

    @Autowired
    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> register(@RequestBody @Valid RegisterUserDto registerDto) {
        User register = registrationService.register(registerDto);
        return ResponseEntity.ok(register);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<RegisterUserDtoErrorResponse> userAlreadyExists(UserAlreadyExistsException exc) {
        RegisterUserDto registerDto = exc.getRegisterDto();
        logger.info("[REGISTRATION] User already exists with email={}", registerDto.getEmail());
        RegisterUserDtoErrorResponse response = new RegisterUserDtoErrorResponse(exc.getMessage(), registerDto);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
