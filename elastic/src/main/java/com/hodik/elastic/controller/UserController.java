package com.hodik.elastic.controller;

import com.hodik.elastic.dto.SearchCriteriaDto;
import com.hodik.elastic.dto.UserDto;
import com.hodik.elastic.exception.EntityAlreadyExistsException;
import com.hodik.elastic.exception.EntityNotFoundException;
import com.hodik.elastic.mapper.UserMapper;
import com.hodik.elastic.model.User;
import com.hodik.elastic.service.EsUserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@Log4j2
public class UserController {
    private final EsUserService userService;
    private final UserMapper userMapper;


    // POST users/ -- create
    // PUT users/{id} -- update
    // GET users -- all
    // GET users/{id}
    // DELETE users/{id}
    // POST users/search -- search by criteria

    @Autowired
    public UserController(EsUserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;

    }


    @PostMapping()
    public ResponseEntity<HttpStatus> createUser(@RequestBody UserDto userDto) throws EntityAlreadyExistsException {
        userService.createUser(userMapper.convertToUser(userDto));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> updateUser(@PathVariable long id, @RequestBody UserDto userDto) {
        userService.update(id, userMapper.convertToUser(userDto));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PutMapping()
    public ResponseEntity<HttpStatus> createUserList(@RequestBody List<UserDto> userDtoList) {
        List<User> users = userDtoList
                .stream()
                .map(userMapper::convertToUser)
                .toList();
        userService.createUserList(users);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping()
    public List<UserDto> getUsers() {
        List<User> users = userService.findAll();
        return users
                .stream()
                .map(userMapper::convertToUserDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable long id) {
        return userMapper.convertToUserDto(userService.findById(id).orElseThrow(EntityNotFoundException::new));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable long id) {
        userService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/search")
    public List<UserDto> searchByCriteria(@RequestBody SearchCriteriaDto searchCriteriaDto) {
        List<User> users = userService.findAllWithFilters(searchCriteriaDto);
        log.info("Search request to index Users " + searchCriteriaDto);
        return users
                .stream()
                .map(userMapper::convertToUserDto)
                .collect(Collectors.toList());
    }


}
