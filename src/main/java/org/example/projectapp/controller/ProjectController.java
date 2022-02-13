package org.example.projectapp.controller;

import org.example.projectapp.controller.dto.ProjectDto;
import org.example.projectapp.service.ProjectService;
import org.example.projectapp.service.exception.ProjectAlredyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("projects")
public class ProjectController {
    Logger logger = LoggerFactory.getLogger(RegistrationController.class);


    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public ResponseEntity<?> createProject(@RequestBody @Valid ProjectDto projectDto) {
        return ResponseEntity.ok(projectService.createProject(projectDto));
    }

    @ExceptionHandler(ProjectAlredyExistsException.class)
    public ResponseEntity<String> projectAlreadyExistsException(ProjectAlredyExistsException ex) {
        String projectName = ex.getProjectName();
        logger.info("[PROJECT] Project already exists with name \"{}\"", projectName);
        return ResponseEntity.badRequest().body(projectName);
    }

}
