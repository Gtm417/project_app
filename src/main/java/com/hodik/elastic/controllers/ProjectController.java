package com.hodik.elastic.controllers;

import com.hodik.elastic.ProjectErrorResponse;
import com.hodik.elastic.dto.ProjectDto;
import com.hodik.elastic.dto.SearchCriteriaDto;
import com.hodik.elastic.exceptions.EntityAlreadyExitsException;
import com.hodik.elastic.mappers.ProjectMapper;
import com.hodik.elastic.model.Project;
import com.hodik.elastic.services.EsProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectController {
    private final EsProjectService projectService;
    private final ProjectMapper projectMapper;

    @Autowired
    public ProjectController(EsProjectService projectService, ProjectMapper projectMapper) {
        this.projectService = projectService;
        this.projectMapper = projectMapper;
    }

    @GetMapping()
    public String test() {
        return "Test!!!";
    }


    @PutMapping("/save")
    public ResponseEntity<HttpStatus> createProject(@RequestBody ProjectDto projectDto) throws EntityAlreadyExitsException {
        projectService.createProject(projectMapper.convertToProject(projectDto));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<HttpStatus> updateProject(@RequestBody ProjectDto projectDto) {
        projectService.updateProject(projectMapper.convertToProject(projectDto));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteProject(@PathVariable("id") long id) {
        projectService.deleteProject(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/all")
    public List<ProjectDto> show() {
        Iterable<Project> projects = projectService.findAll();
        return getProjectDtos(projects);
    }

    private List<ProjectDto> getProjectDtos(Iterable<Project> projects) {
        List<ProjectDto> projectList = new ArrayList<>();
        projects.forEach(x -> projectList.add(projectMapper.convertToProjectDto(x)));
        return projectList;
    }

    @PostMapping("/find")
    public List<ProjectDto> findByFilters(@RequestBody SearchCriteriaDto searchCriteriaDto) {
        Iterable<Project> projects = projectService.findAllWithFilters(searchCriteriaDto);
        return getProjectDtos(projects);
    }

    @ExceptionHandler
    private ResponseEntity<ProjectErrorResponse> exceptionHandler(EntityAlreadyExitsException e) {
        ProjectErrorResponse response = new ProjectErrorResponse(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
