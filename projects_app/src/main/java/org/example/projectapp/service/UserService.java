package org.example.projectapp.service;

import org.example.projectapp.controller.dto.SearchDto;
import org.example.projectapp.model.User;
import org.example.projectapp.service.dto.UserDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {
    Page<UserDto> findUsers(SearchDto searchDto);

    List<User> findAllUsers();

    List<User> findUsersByListId(List<Long> listID);

}
