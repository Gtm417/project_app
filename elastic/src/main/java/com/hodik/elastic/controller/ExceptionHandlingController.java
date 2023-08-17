package com.hodik.elastic.controller;

import com.hodik.elastic.exception.EntityAlreadyExistsException;
import com.hodik.elastic.exception.EntityNotFoundException;
import com.hodik.elastic.exception.ErrorResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Log4j2
public class ExceptionHandlingController {

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> exceptionHandler(EntityAlreadyExistsException e) {
        ErrorResponse message = new ErrorResponse(e.getMessage());
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> exceptionHandler(EntityNotFoundException e) {
        ErrorResponse message = new ErrorResponse(e.getMessage());
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> exceptionHandler(IllegalArgumentException e) {
        ErrorResponse message = new ErrorResponse(e.getMessage());
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }
}
