package org.example.projectapp.controller;

import org.example.projectapp.controller.dto.SearchDto;
import org.example.projectapp.service.UserService;
import org.example.projectapp.service.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("users")
public class UsersController {
    private final UserService userService;

    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/search")
    @Transactional
    public ResponseEntity<Page<UserDto>> findUsers(@RequestBody @Valid SearchDto searchDto) {
        return ResponseEntity.ok(userService.findUsers(searchDto));
    }
}
