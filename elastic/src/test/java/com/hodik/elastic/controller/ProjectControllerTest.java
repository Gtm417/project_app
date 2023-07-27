package com.hodik.elastic.controller;

import com.google.gson.Gson;
import com.hodik.elastic.dto.ProjectDto;
import com.hodik.elastic.dto.SearchCriteriaDto;
import com.hodik.elastic.exception.EntityAlreadyExistsException;
import com.hodik.elastic.exception.EntityNotFoundException;
import com.hodik.elastic.mapper.ProjectMapper;
import com.hodik.elastic.model.Project;
import com.hodik.elastic.service.EsProjectService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.elasticsearch.core.ResourceUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectControllerTest {

    public static final LocalDate START_DATE = LocalDate.of(2020, 1, 8);
    public static final long ID = 1L;
    public static final String NAME = "Name";
    public static final String CATEGORY = "Category";
    public static final LocalDate CREATED_DATE = LocalDate.of(2020, 7, 5);
    public static final String DESCRIPTION = "Description";
    public static final LocalDate FINAL_PLANNED_DATE = LocalDate.of(2025, 12, 31);
    public static final String IS_COMMERCIAL = "Commercial";
    public static final boolean IS_PRIVATE = false;
    public static final String STATUS = "Status";

    private final Project expectedProject = getExpectedProject();
    private final List<Project> expectedProjectList = List.of(expectedProject);

    private final ProjectDto expectedProjectDto = getProjectDto();
    private final List<ProjectDto> expectedProjectDtoList = List.of(expectedProjectDto);
    private final Gson gson = new Gson();
    private final SearchCriteriaDto searchCriteriaDto = gson.fromJson(ResourceUtil.readFileFromClasspath("search.criteria.project.success.json"), SearchCriteriaDto.class);

    @Mock
    private EsProjectService projectService;

    @Mock
    private ProjectMapper projectMapper;


    @InjectMocks
    private ProjectController projectController;

    @Test
    void shouldCreateProject() throws EntityAlreadyExistsException {
        //given
        when(projectMapper.convertToProject(expectedProjectDto)).thenReturn(expectedProject);
        //when
        ResponseEntity<HttpStatus> response = projectController.createProject(expectedProjectDto);
        //then
        verify(projectService).createProject(expectedProject);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void shouldThrowExceptionWhenProjectAlreadyExists() throws EntityAlreadyExistsException {
        //given
        when(projectMapper.convertToProject(expectedProjectDto)).thenReturn(expectedProject);
        Mockito.doThrow(EntityAlreadyExistsException.class).when(projectService).createProject(expectedProject);
        //when
        EntityAlreadyExistsException exception = assertThrows(EntityAlreadyExistsException.class,
                () -> projectController.createProject(expectedProjectDto));
        //then
        assertEquals(EntityAlreadyExistsException.class, exception.getClass());
    }

    @Test
    void shouldUpdateProject() {
        //given
        when(projectMapper.convertToProject(expectedProjectDto)).thenReturn(expectedProject);
        //when
        ResponseEntity<HttpStatus> response = projectController.updateProject(expectedProject.getId(), expectedProjectDto);
        //then
        verify(projectService).updateProject(expectedProject.getId(), expectedProject);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void shouldDeleteProject() {
        //when
        ResponseEntity<HttpStatus> response = projectController.deleteProject(expectedProject.getId());
        //then
        verify(projectService).deleteProject(expectedProject.getId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void shouldReturnProjectDtoList() {
        //given
        when(projectService.findAll()).thenReturn(expectedProjectList);
        when(projectMapper.convertToProjectDto(expectedProject)).thenReturn(expectedProjectDto);
        //when
        List<ProjectDto> projectDtoList = projectController.getProjects();
        //then
        verify(projectService).findAll();
        verify(projectMapper, atLeast(1)).convertToProjectDto(expectedProject);
        assertEquals(expectedProjectDtoList, projectDtoList);
    }

    @Test
    void shouldReturnProjectDto() {
        //given
        when(projectService.findById(expectedProject.getId())).thenReturn(Optional.of(expectedProject));
        when(projectMapper.convertToProjectDto(expectedProject)).thenReturn(expectedProjectDto);
        //when
        ProjectDto projectDto = projectController.getProject(expectedProject.getId());
        //then
        verify(projectService).findById(expectedProject.getId());
        verify(projectMapper, atLeast(1)).convertToProjectDto(expectedProject);
        assertEquals(expectedProjectDto, projectDto);
    }

    @Test
    void shouldThrowExceptionWhenProjectNotFound() {
        //given
        when(projectService.findById(expectedProject.getId())).thenThrow(EntityNotFoundException.class);
        //when
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> projectController.getProject(expectedProject.getId()));
        //then
        verify(projectService).findById(expectedProject.getId());
        assertEquals(EntityNotFoundException.class, exception.getClass());
    }

    @Test
    void shouldReturnProjectDtoListByFilters() {
        //given
        when(projectService.findAllWithFilters(any(SearchCriteriaDto.class))).thenReturn(expectedProjectList);
        when(projectMapper.convertToProjectDto(expectedProject)).thenReturn(expectedProjectDto);
        //when
        List<ProjectDto> projectDtoList = projectController.findByFilters(searchCriteriaDto);
        //then
        verify(projectService).findAllWithFilters(searchCriteriaDto);
        verify(projectMapper, atLeast(1)).convertToProjectDto(expectedProject);
        assertEquals(expectedProjectDtoList, projectDtoList);
    }


    private Project getExpectedProject() {
        return Project.builder()
                .id(ID)
                .name(NAME)
                .category(CATEGORY)
                .createdDate(CREATED_DATE)
                .description(DESCRIPTION)
                .finalPlannedDate(FINAL_PLANNED_DATE)
                .isCommercial(IS_COMMERCIAL)
                .isPrivate(IS_PRIVATE)
                .startDate(START_DATE)
                .status(STATUS)
                .build();
    }

    private ProjectDto getProjectDto() {
        return ProjectDto.builder()
                .id(ID)
                .name(NAME)
                .category(CATEGORY)
                .createdDate(CREATED_DATE)
                .description(DESCRIPTION)
                .finalPlannedDate(FINAL_PLANNED_DATE)
                .isCommercial(IS_COMMERCIAL)
                .isPrivate(IS_PRIVATE)
                .startDate(START_DATE)
                .status(STATUS)
                .build();
    }
}