package org.example.projectapp.controller;

import org.example.projectapp.controller.dto.SearchDto;
import org.example.projectapp.restclient.ElasticServiceClient;
import org.example.projectapp.service.UserService;
import org.example.projectapp.service.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("users")
public class UsersController {
    private final UserService userService;
    private final ElasticServiceClient elasticClient;

    public UsersController(UserService userService, ElasticServiceClient elasticServiceClient) {
        this.userService = userService;
        this.elasticClient = elasticServiceClient;
    }

    @PostMapping("/search")
    public ResponseEntity<Page<UserDto>> findUsers(@RequestBody @Valid SearchDto searchDto) {
        return ResponseEntity.ok(userService.findUsers(searchDto));
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers() {
        return ResponseEntity.ok(elasticClient.getUsers());
    }
}
