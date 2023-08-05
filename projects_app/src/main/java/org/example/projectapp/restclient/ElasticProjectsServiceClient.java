package org.example.projectapp.restclient;

import org.example.projectapp.controller.dto.ProjectDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "${service.elastic.projects.feign.name}", url = "${service.elastic.feign.url")
public interface ElasticProjectsServiceClient {
    @GetMapping("/projects")
    List<ProjectDto> getProjects();
}
