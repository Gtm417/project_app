package com.hodik.elastic.controllers;

import com.hodik.elastic.dto.ProjectDto;
import com.hodik.elastic.dto.SearchCriteriaDto;
import com.hodik.elastic.dto.SearchFilter;
import com.hodik.elastic.dto.SearchSort;
import com.hodik.elastic.exceptions.EntityAlreadyExistsException;
import com.hodik.elastic.exceptions.EntityNotFoundException;
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
import java.util.List;
import java.util.Optional;

import static com.hodik.elastic.util.Operations.LIKE;
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
    private final SearchSort searchSort = new SearchSort("Name", true);
    private final List<SearchSort> searchSortList = List.of(searchSort);
    private final SearchFilter searchFilter = new SearchFilter("Name", LIKE, List.of("Name"));
    private final SearchCriteriaDto searchCriteriaDto = new SearchCriteriaDto(List.of(searchFilter), 0, 2, searchSortList);

    @Mock
    private EsProjectService projectService;

    @Mock
    private ProjectMapper projectMapper;


    @InjectMocks
    private ProjectController projectController;

    @Test
    void createProject() throws EntityAlreadyExistsException {
        //given
        when(projectMapper.convertToProject(expectedProjectDto)).thenReturn(expectedProject);
        //when
        ResponseEntity<HttpStatus> response = projectController.createProject(expectedProjectDto);
        //then
        verify(projectService).createProject(expectedProject);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void createProjectException() throws EntityAlreadyExistsException {
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
    void updateProject() {
        //given
        when(projectMapper.convertToProject(expectedProjectDto)).thenReturn(expectedProject);
        //when
        ResponseEntity<HttpStatus> response = projectController.updateProject(expectedProject.getId(),expectedProjectDto);
        //then
        verify(projectService).updateProject(expectedProject.getId(),expectedProject);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void deleteProject() {
        //when
        ResponseEntity<HttpStatus> response = projectController.deleteProject(expectedProject.getId());
        //then
        verify(projectService).deleteProject(expectedProject.getId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getProjects() {
        //given
        when(projectService.findAll()).thenReturn(expectedProjectList);
        when(projectMapper.convertToProjectDto(expectedProject)).thenReturn(expectedProjectDto);
        //when
        List<ProjectDto> projectDtoList= projectController.getProjects();
        //then
        verify(projectService).findAll();
        verify(projectMapper, atLeast(1)).convertToProjectDto(expectedProject);
        assertEquals(expectedProjectDtoList, projectDtoList);
    }

    @Test
    void getProjectSuccess() {
        //given
        when(projectService.findById(expectedProject.getId())).thenReturn(Optional.of(expectedProject));
        when(projectMapper.convertToProjectDto(expectedProject)).thenReturn(expectedProjectDto);
        //when
        ProjectDto projectDto= projectController.getProject(expectedProject.getId());
        //then
        verify(projectService).findById(expectedProject.getId());
        verify(projectMapper, atLeast(1)).convertToProjectDto(expectedProject);
        assertEquals(expectedProjectDto, projectDto);
    }
    @Test
    void getProjectException() {
        //given
        when(projectService.findById(expectedProject.getId())).thenThrow(EntityNotFoundException.class);
        //when
        EntityNotFoundException exception= assertThrows(EntityNotFoundException.class,()-> projectController.getProject(expectedProject.getId()));
        //then
        verify(projectService).findById(expectedProject.getId());
        assertEquals(EntityNotFoundException.class, exception.getClass());
    }

    @Test
    void findByFilters() {
        //given
        when(projectService.findAllWithFilters(any(SearchCriteriaDto.class))).thenReturn(expectedProjectList);
        when(projectMapper.convertToProjectDto(expectedProject)).thenReturn(expectedProjectDto);
        //when
        List<ProjectDto> projectDtoList= projectController.findByFilters(searchCriteriaDto);
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