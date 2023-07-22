package org.example.projectapp.restclient;

import org.example.projectapp.service.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "${service.elastic.feign.name}", url = "${service.elastic.feign.url}")
public interface ElasticServiceClient {
    @GetMapping("/users")
    List<UserDto> getUsers();
}
