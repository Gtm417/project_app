package com.hodik.elastic.controllers;

import com.hodik.elastic.dto.SearchCriteriaDto;
import com.hodik.elastic.dto.UserDto;
import com.hodik.elastic.mappers.UserMapper;
import com.hodik.elastic.model.User;
import com.hodik.elastic.services.EsUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    private final EsUserService userService;
    private final UserMapper userMapper;

    @Autowired
    public UserController(EsUserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }


    @PutMapping("/save")
    public ResponseEntity<HttpStatus> createUser(@RequestBody UserDto userDto) {
        userService.save(userMapper.convertToUser(userDto));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<HttpStatus> updateUser(@RequestBody UserDto userDto) {
        userService.update(userMapper.convertToUser(userDto));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/all")
    public List<UserDto> show() {
        List<UserDto> userDtoList = new ArrayList<>();
        Iterable<User> users = userService.findAll();
        for (User user : users) {
            userDtoList.add(userMapper.convertToUserDto(user));
        }
        return userDtoList;
    }

    @DeleteMapping("/id")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable long id) {
        userService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
    @PostMapping("/find")
    public List<UserDto> findByFilters (@RequestBody SearchCriteriaDto searchCriteriaDto){
return null;
    }
}
