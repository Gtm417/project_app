package org.example.projectapp.restclient;

import org.example.projectapp.mapper.dto.SearchElasticCriteriaDto;
import org.example.projectapp.mapper.dto.UserElasticDto;
import org.example.projectapp.service.dto.UserDto;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "${service.elastic.users.feign.name}", url = "${service.elastic.feign.url}/users")
@LoadBalancerClient
public interface ElasticUsersServiceClient {
    @GetMapping
    List<UserDto> getUsers();

    @PostMapping
    void createUser(UserElasticDto userElasticDto);

    @PutMapping
    void createUserList(List<UserElasticDto> userElasticDtoList);

    @PutMapping("/{id}")
    void updateUser(@PathVariable long id, UserElasticDto userElasticDto);

    @PostMapping("/search")
    List<UserElasticDto> searchUsers(@RequestBody SearchElasticCriteriaDto searchElasticCriteriaDto);

    @GetMapping("/test")
    String test();
}


