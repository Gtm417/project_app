package org.example.projectapp.controller;

import org.example.projectapp.mapper.UserMapper;
import org.example.projectapp.mapper.dto.UserElasticDto;
import org.example.projectapp.model.User;
import org.example.projectapp.restclient.ElasticUsersServiceClient;
import org.example.projectapp.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class RebaseUsersToElasticController {
    private final UserService userService;
    private final ElasticUsersServiceClient elasticUsersServiceClient;
    private final UserMapper userMapper;

    public RebaseUsersToElasticController(UserService userService, ElasticUsersServiceClient elasticUsersServiceClient, UserMapper userMapper) {
        this.userService = userService;
        this.elasticUsersServiceClient = elasticUsersServiceClient;
        this.userMapper = userMapper;
    }

    @PostMapping("/sync")
    public ResponseEntity<HttpStatus> rebaseUsers(@RequestBody @Nullable List<Long> ids) {
        List<User> users;
        if (!CollectionUtils.isEmpty(ids)) {
            users = userService.findUsersByListId(ids);
        } else {
            users = userService.findAllUsers();
        }
        List<UserElasticDto> userElasticDtoList = users.stream().map(userMapper::convertToUserElasticDto)
                .collect(Collectors.toList());
        elasticUsersServiceClient.createUserList(userElasticDtoList);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
