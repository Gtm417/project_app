package org.example.projectapp.restclient;

import org.example.projectapp.mapper.dto.UserElasticDto;
import org.example.projectapp.service.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;

@FeignClient(name = "${service.elastic.feign.name}", url = "${service.elastic.feign.url}")
public interface ElasticUsersServiceClient {
    @GetMapping("/users")
    List<UserDto> getUsers();

    @PostMapping("/users")
    void createUser(UserElasticDto userElasticDto);

    @PutMapping("/users")
    void createUserList(List<UserElasticDto> userElasticDtoList);

    @PutMapping("/users/{id}")
    void updateUser(@PathVariable long id, UserElasticDto userElasticDto);
}
