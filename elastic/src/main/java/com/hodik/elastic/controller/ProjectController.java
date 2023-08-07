package com.hodik.elastic.controller;

import com.hodik.elastic.dto.ProjectDto;
import com.hodik.elastic.dto.SearchCriteriaDto;
import com.hodik.elastic.exception.EntityAlreadyExistsException;
import com.hodik.elastic.exception.EntityNotFoundException;
import com.hodik.elastic.exception.ProjectErrorResponse;
import com.hodik.elastic.mapper.ProjectMapper;
import com.hodik.elastic.service.EsProjectService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/projects")
@Log4j2
public class ProjectController {
    private final EsProjectService projectService;
    private final ProjectMapper projectMapper;


    @Autowired
    public ProjectController(EsProjectService projectService, ProjectMapper projectMapper) {
        this.projectService = projectService;
        this.projectMapper = projectMapper;

    }

    @PostMapping()
    public ResponseEntity<HttpStatus> createProject(@RequestBody ProjectDto projectDto) throws EntityAlreadyExistsException {
        projectService.createProject(projectMapper.convertToProject(projectDto));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/sync")
    public ResponseEntity<HttpStatus> createProjectList(@RequestBody List<ProjectDto> projectDtoList) {
        projectService.createProjectList(projectDtoList.stream().map(projectMapper::convertToProject).toList());
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> updateProject(@PathVariable long id, @RequestBody ProjectDto projectDto) {
        projectService.updateProject(id, projectMapper.convertToProject(projectDto));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteProject(@PathVariable("id") long id) {
        projectService.deleteProject(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping()
    public List<ProjectDto> getProjects() {
        return projectService.findAll().stream().map(projectMapper::convertToProjectDto).collect(Collectors.toList());

    }

    @GetMapping("/{id}")
    public ProjectDto getProject(@PathVariable long id) {
        return projectMapper.convertToProjectDto(projectService.findById(id).orElseThrow(EntityNotFoundException::new));
    }

    @PostMapping("/search")
    public List<ProjectDto> findByFilters(@RequestBody SearchCriteriaDto searchCriteriaDto) {
        log.info("Search request to index Projects "  + searchCriteriaDto);
        return projectService.findAllWithFilters(searchCriteriaDto).stream()
                .map(projectMapper::convertToProjectDto).collect(Collectors.toList());

    }

    @ExceptionHandler
    private ResponseEntity<ProjectErrorResponse> exceptionHandler(EntityAlreadyExistsException e) {
        ProjectErrorResponse response = new ProjectErrorResponse(e.getMessage());
        log.error(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<ProjectErrorResponse> exceptionHandler(IllegalArgumentException e) {
        ProjectErrorResponse response = new ProjectErrorResponse(e.getMessage());
        log.error(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<ProjectErrorResponse> exceptionHandler(EntityNotFoundException e) {
        ProjectErrorResponse response = new ProjectErrorResponse(e.getMessage());
        log.error(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
