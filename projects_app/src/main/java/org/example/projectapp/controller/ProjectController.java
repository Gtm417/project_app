package org.example.projectapp.controller;

import org.example.projectapp.controller.dto.ProjectDto;
import org.example.projectapp.controller.dto.ProjectInfoDto;
import org.example.projectapp.controller.dto.SearchDto;
import org.example.projectapp.restclient.ElasticProjectsServiceClient;
import org.example.projectapp.service.ProjectService;
import org.example.projectapp.service.VacancyService;
import org.example.projectapp.service.dto.ProjectResponseDto;
import org.example.projectapp.service.exception.ProjectAlreadyExistsException;
import org.example.projectapp.service.exception.ProjectNotMatchException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("projects")
public class ProjectController {
    private static final Logger logger = LoggerFactory.getLogger(RegistrationController.class);

    private final ProjectService projectService;
    private final ElasticProjectsServiceClient elasticProjectServiceClient;
    private final VacancyService vacancyService;

    public ProjectController(ProjectService projectService, ElasticProjectsServiceClient elasticProjectServiceClient, VacancyService vacancyService) {
        this.projectService = projectService;
        this.elasticProjectServiceClient = elasticProjectServiceClient;
        this.vacancyService = vacancyService;
    }

    @PostMapping
    public ResponseEntity<?> createProject(@RequestBody @Valid ProjectDto projectDto) {
        return ResponseEntity.ok(projectService.createProject(projectDto));
    }


    @PutMapping("/{id}/notification")
    public ResponseEntity<?> createProject(@PathVariable("id") Long id,
                                           @RequestBody @Valid @NotNull(message = "Should not be empty") Boolean enable) {
        projectService.enableNotification(id, enable);
        return ResponseEntity.ok().build();
    }

    //todo [FRONT] send json with not blank current fields.
    // Send empty value {""} if field was cleared by user for nullable fields {description, startDate, finalDate}
    @PutMapping("/{id}/info")
    public ResponseEntity<?> updateProjectInfo(@PathVariable("id") Long id,
                                               @RequestBody @Valid ProjectInfoDto projectInfoDto) {
        ProjectResponseDto projectResponseDto = projectService.updateProjectInfo(id, projectInfoDto);
        return ResponseEntity.ok(projectResponseDto);
    }

    @DeleteMapping("/{id}/vacancies/{vacancyId}")
    public ResponseEntity<HttpStatus> deleteVacancy(@PathVariable Long id, @PathVariable Long vacancyId) {
        vacancyService.deleteVacancy(id, vacancyId);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PutMapping("/{id}/private")
    public ResponseEntity<?> makeProjectPrivate(@PathVariable("id") Long id,
                                                @RequestBody @Valid @NotNull(message = "Should not be empty") Boolean isPrivate) {
        ProjectResponseDto projectResponseDto = projectService.makeProjectPrivate(id, isPrivate);
        return ResponseEntity.ok(projectResponseDto);
    }

    @GetMapping
    public ResponseEntity<List<ProjectResponseDto>> getProjects() {
        return ResponseEntity.ok(elasticProjectServiceClient.getProjects());
    }

    @PostMapping("/search/db")
    public ResponseEntity<Page<ProjectResponseDto>> searchProject(@RequestBody @Valid SearchDto searchDto) {
        Page<ProjectResponseDto> projectsByFilters = projectService.findProjectsByFilters(searchDto);
        return ResponseEntity.ok(projectsByFilters);
    }

    @PostMapping("/search/1")
    public ResponseEntity<List<ProjectResponseDto>> searchProjectInElastic(@RequestBody @Valid SearchDto searchDto) {
        List<ProjectResponseDto> projectsFromElastic = projectService.findProjectsInElastic(searchDto);
        return ResponseEntity.ok(projectsFromElastic);
    }

    @ExceptionHandler(ProjectAlreadyExistsException.class)
    public ResponseEntity<String> projectAlreadyExistsException(ProjectAlreadyExistsException ex) {
        String projectName = ex.getProjectName();
        logger.info("[PROJECT] Project already exists with name \"{}\"", projectName);
        return ResponseEntity.badRequest().body(projectName);
    }

    @ExceptionHandler(ProjectNotMatchException.class)
    public ResponseEntity<String> projectNotMatchException(ProjectNotMatchException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

}
