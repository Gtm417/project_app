package org.example.projectapp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.projectapp.controller.dto.RegisterUserDto;
import org.example.projectapp.controller.dto.ValidationErrorResponse;
import org.example.projectapp.model.UserType;
import org.junit.jupiter.api.Test;
import org.springframework.data.geo.Distance;
import org.springframework.validation.FieldError;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ValidationServiceTest {
    private final ValidationService validationService = new ValidationService();

    @Test
    void shouldAddAllMessages() throws JsonProcessingException {
        //given
        String string = new ObjectMapper().writeValueAsString(UserType.STUDENT);
        System.out.println(string);
        List<String> messages = List.of("message1", "message2", "message3");
        List<FieldError> fieldErrors = List.of(
                new FieldError("test", "field1", messages.get(0)),
                new FieldError("test", "field1", messages.get(1)),
                new FieldError("test", "field1", messages.get(2)),
                new FieldError("test", "field2", messages.get(2)),
                new FieldError("test", "field2", messages.get(0))
        );
        //when
        ValidationErrorResponse actualValidationError = validationService.mapErrors(fieldErrors);

        //then
        Map<String, Set<String>> errors = actualValidationError.getErrors();
        Set<String> field2Messages = errors.get("field2");
        assertEquals(new HashSet<>(messages), errors.get("field1"));
        assertTrue(field2Messages.contains(messages.get(2)));
        assertTrue(field2Messages.contains(messages.get(0)));
        assertFalse(field2Messages.contains(messages.get(1)));
    }

    @Test
    void test() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        RegisterUserDto registerUserDto = objectMapper.readValue("{\n" +
                "    \"firstName\":\"Tima\",\n" +
                "    \"lastName\":\"Tima\",\n" +
                "    \"email\":\"tima@gmail.com\",\n" +
                "    \"password\":\"timapsw\",\n" +
                "    \"type\": \"MENTOR\",\n" +
                "    \"role\": null\n" +
                "}", RegisterUserDto.class);
        System.out.println(registerUserDto);

        System.out.println(objectMapper.writeValueAsString(registerUserDto));

    }
}