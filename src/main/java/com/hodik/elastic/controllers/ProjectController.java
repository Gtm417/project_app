package com.hodik.elastic.controllers;

import com.hodik.elastic.ProjectErrorResponse;
import com.hodik.elastic.dto.ProjectDto;
import com.hodik.elastic.exceptions.EntityAlreadyExitsException;
import com.hodik.elastic.mappers.ProjectMapper;
import com.hodik.elastic.model.Project;
import com.hodik.elastic.services.EsProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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


    @PostMapping("/save")
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
    public Iterable<Project> show() {
        //todo Dto
        return projectService.findAll();
    }

//    @PostMapping("/find")
//    public Iterable<Project> findByFilters(@RequestBody SearchCriteriaDto searchCriteriaDto,
//                                           @PageableDefault(sort = {"id"}, direction = Sort.Direction.ASC, size = 5) Pageable pageable){
//        return projectService.findAllWithFilters(searchCriteriaDto, pageable);
//    }

    @ExceptionHandler
    private ResponseEntity<ProjectErrorResponse> exceptionHandler(EntityAlreadyExitsException e) {
        ProjectErrorResponse response= new ProjectErrorResponse(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
