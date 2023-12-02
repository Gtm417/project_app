package com.hodik.elastic.controller;

import com.hodik.elastic.dto.ProjectDto;
import com.hodik.elastic.dto.SearchCriteriaDto;
import com.hodik.elastic.dto.SearchDto;
import com.hodik.elastic.exception.EntityAlreadyExistsException;
import com.hodik.elastic.exception.EntityNotFoundException;
import com.hodik.elastic.mapper.ProjectMapper;
import com.hodik.elastic.model.Project;
import com.hodik.elastic.service.EsProjectService;
import jakarta.validation.Valid;
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

    @PostMapping
    public ResponseEntity<HttpStatus> createProject(@RequestBody ProjectDto projectDto) throws EntityAlreadyExistsException {
        projectService.createProject(projectMapper.convertToProject(projectDto));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/sync")
    public ResponseEntity<HttpStatus> syncProjectList(@RequestBody List<ProjectDto> projectDtoList) {
        List<Project> projects = projectDtoList.stream().map(projectMapper::convertToProject).toList();
        projectService.createProjectList(projects);
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

    @GetMapping
    public List<ProjectDto> getProjects() {
        return getProjectDtoList(projectService.findAll());

    }

    @GetMapping("/{id}")
    public ProjectDto getProject(@PathVariable long id) {
        return projectMapper.convertToProjectDto(projectService.findById(id).orElseThrow(EntityNotFoundException::new));
    }

    @PostMapping("/search/1")
    public List<ProjectDto> findByFilters(@RequestBody SearchCriteriaDto searchCriteriaDto) {
        log.info("Search request to index Projects {}", searchCriteriaDto);
        List<Project> allWithFilters = projectService.findAllWithFilters(searchCriteriaDto);
        return getProjectDtoList(allWithFilters);
    }

    private List<ProjectDto> getProjectDtoList(List<Project> allWithFilters) {
        return allWithFilters.stream()
                .map(projectMapper::convertToProjectDto)
                .collect(Collectors.toList());
    }

    @PostMapping("/search")
    public ResponseEntity<List<ProjectDto>> searchProjectsInElastic(@RequestBody @Valid SearchDto searchDto) {
        log.info("Search request to index Projects");
        List<Project> projects = projectService.findAllWithSearch(searchDto);
        return ResponseEntity.ok(getProjectDtoList(projects));
    }
}
