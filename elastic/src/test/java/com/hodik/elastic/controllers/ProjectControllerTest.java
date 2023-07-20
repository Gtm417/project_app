package com.hodik.elastic.controllers;

import com.hodik.elastic.dto.ProjectDto;
import com.hodik.elastic.exceptions.EntityAlreadyExistsException;
import com.hodik.elastic.mappers.PageableMapper;
import com.hodik.elastic.mappers.ProjectMapper;
import com.hodik.elastic.model.Project;
import com.hodik.elastic.services.EsProjectService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProjectControllerTest {
    private final Project PROJECT = Project.builder()
            .id(1L)
            .name("Name")
            .category("Category")
            .createdDate(LocalDate.of(2020, 7, 5))
            .description("Description")
            .finalPlannedDate(LocalDate.of(2025, 12, 31))
            .isCommercial("Commercial")
            .isPrivate(false)
            .startDate(LocalDate.of(2020, 1, 8))
            .status("Status")
            .build();
    private final ProjectDto PROJECT_DTO = ProjectDto.builder()
            .id(1L)
            .name("Name")
            .category("Category")
            .createdDate(LocalDate.of(2020, 7, 5))
            .description("Description")
            .finalPlannedDate(LocalDate.of(2025, 12, 31))
            .isCommercial("Commercial")
            .isPrivate(false)
            .startDate(LocalDate.of(2020, 1, 8))
            .status("Status")
            .build();
    @Mock
    private EsProjectService projectService;
    @Mock
    private ProjectMapper projectMapper;
    @Mock
    private PageableMapper pageableMapper;
    @InjectMocks
    private ProjectController projectController;

    @Test
    void createProject() throws EntityAlreadyExistsException {
        //given
        when(projectMapper.convertToProject(PROJECT_DTO)).thenReturn(PROJECT);
        //when
        ResponseEntity<HttpStatus> response = projectController.createProject(PROJECT_DTO);
        //then
        verify(projectService).createProject(PROJECT);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void createProjectException() throws EntityAlreadyExistsException {
        //given
        when(projectMapper.convertToProject(PROJECT_DTO)).thenReturn(PROJECT);
      Mockito.doThrow(EntityAlreadyExistsException.class).when(projectService).createProject(PROJECT);
        //when
        EntityAlreadyExistsException exception = assertThrows(EntityAlreadyExistsException.class,
                () -> projectController.createProject(PROJECT_DTO));
        //then
        assertEquals(EntityAlreadyExistsException.class, exception.getClass());
    }

    @Test
    void updateProject() {
    }

    @Test
    void deleteProject() {
    }

    @Test
    void getProjects() {
    }

    @Test
    void getProject() {
    }

    @Test
    void findByFilters() {
    }
}