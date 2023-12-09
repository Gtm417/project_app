package org.example.projectapp.controller;

import org.example.projectapp.auth.exception.UserNotFoundException;
import org.example.projectapp.controller.dto.ValidationErrorResponse;
import org.example.projectapp.service.ValidationService;
import org.example.projectapp.service.exception.CustomEntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@ControllerAdvice
public class ExceptionHandlingController {
    Logger logger = LoggerFactory.getLogger(ExceptionHandlingController.class);

    private final ValidationService validationService;

    public ExceptionHandlingController(ValidationService validationService) {
        this.validationService = validationService;
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<ValidationErrorResponse> onMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        ValidationErrorResponse validationErrorResponse = validationService.mapErrors(fieldErrors);
        logger.info("[VALIDATION] Fields contain validation errors {}", validationErrorResponse.getErrors());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(validationErrorResponse);
    }


    @ExceptionHandler(UserNotFoundException.class)
    @ResponseBody
    public ResponseEntity<?> userNotFoundHandling(UserNotFoundException e) {
        String email = e.getEmail();
        logger.info("[NOT_FOUND] User not found with email {}", email);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(email);
    }

    @ExceptionHandler(CustomEntityNotFoundException.class)
    public ResponseEntity<?> EntityNotException(CustomEntityNotFoundException ex) {
        String className = ex.getClassName();
        Long id = ex.getEntityId();
        logger.info("[ENTITY][NOT_FOUND] Entity {} not found with id={}", className, id);
        //todo there isn't any message only 404
        return ResponseEntity.notFound().build();
    }


}
