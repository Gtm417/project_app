package org.example.projectapp.restclient;

import org.example.projectapp.controller.dto.ProjectDto;
import org.example.projectapp.mapper.dto.ProjectElasticDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;

@FeignClient(name = "${service.elastic.projects.feign.name}", url = "${service.elastic.feign.url}/projects")
public interface ElasticProjectsServiceClient {
    @GetMapping
    List<ProjectDto> getProjects();

    @PostMapping("/sync")
    void createProjectList(List<ProjectElasticDto> projectDtoList);

    @PostMapping
    void createProject(ProjectElasticDto projectElasticDto);

    @PutMapping("/{id}")
    void updateProject(@PathVariable long id, ProjectElasticDto projectElasticDto);
}
