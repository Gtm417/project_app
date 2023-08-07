package org.example.projectapp.controller;

import org.example.projectapp.mapper.ProjectMapper;
import org.example.projectapp.mapper.dto.ProjectElasticDto;
import org.example.projectapp.model.Project;
import org.example.projectapp.restclient.ElasticProjectsServiceClient;
import org.example.projectapp.service.ProjectService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/projects")
public class RebaseProjectsToElasticController {
    private final ProjectService projectService;
    private final ProjectMapper projectMapper;
    private final ElasticProjectsServiceClient elasticProjectsServiceClient;

    public RebaseProjectsToElasticController(ProjectService projectService, ProjectMapper projectMapper, ElasticProjectsServiceClient elasticProjectsServiceClient) {
        this.projectService = projectService;
        this.projectMapper = projectMapper;
        this.elasticProjectsServiceClient = elasticProjectsServiceClient;
    }


    @PostMapping("/sync")
    public HttpEntity<HttpStatus> rebaseProjects(@RequestBody @Nullable List<Long> ids) {
        List<Project> projects;
        if (!CollectionUtils.isEmpty(ids)) {
            projects = projectService.findProjectsByListId(ids);
        } else {
            projects = projectService.findAllProjects();
        }
        List<ProjectElasticDto> projectDtoList = projects.stream().map(projectMapper::convertToProjectElasticDto)
                .collect(Collectors.toList());
        elasticProjectsServiceClient.createProjectList(projectDtoList);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

