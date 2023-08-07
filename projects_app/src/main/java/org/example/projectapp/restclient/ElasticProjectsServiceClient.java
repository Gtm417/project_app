package org.example.projectapp.restclient;

import org.example.projectapp.controller.dto.ProjectDto;
import org.example.projectapp.mapper.dto.ProjectElasticDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(name = "${service.elastic.projects.feign.name}", url = "${service.elastic.feign.url}")
public interface ElasticProjectsServiceClient {
    @GetMapping("/projects")
    List<ProjectDto> getProjects();

    @PostMapping("/projects/sync")
    void createProjectList(List<ProjectElasticDto> projectDtoList);
}
