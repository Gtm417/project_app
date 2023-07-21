package org.example.projectapp.service;

import org.example.projectapp.controller.dto.SearchDto;
import org.example.projectapp.service.dto.UserDto;
import org.springframework.data.domain.Page;

public interface UserService {
    Page<UserDto> findUsers(SearchDto searchDto);
}
