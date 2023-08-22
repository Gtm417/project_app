package com.hodik.elastic.service;

import com.google.gson.Gson;
import com.hodik.elastic.dto.SearchCriteriaDto;
import com.hodik.elastic.dto.SearchSort;
import com.hodik.elastic.exception.EntityAlreadyExistsException;
import com.hodik.elastic.mapper.PageableMapper;
import com.hodik.elastic.model.Project;
import com.hodik.elastic.model.ProjectStatus;
import com.hodik.elastic.repository.ProjectRepository;
import com.hodik.elastic.repository.ProjectSearchRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ResourceUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EsProjectServiceTest {
    public static final LocalDateTime START_DATE = LocalDateTime.of(2020, 1, 8, 0, 0);
    public static final long ID = 1L;
    public static final String NAME = "Name";
    public static final String CATEGORY = "Category";
    public static final LocalDateTime CREATED_DATE = LocalDateTime.of(2020, 7, 5, 0, 0);
    public static final String DESCRIPTION = "Description";
    public static final LocalDateTime FINAL_PLANNED_DATE = LocalDateTime.of(2025, 12, 31, 0, 0);
    public static final boolean IS_COMMERCIAL = true;
    public static final boolean IS_PRIVATE = false;
    public static final ProjectStatus STATUS = ProjectStatus.NEW;
    public static final int PAGE = 0;
    public static final int SIZE = 2;

    private final String searchCriteria = ResourceUtil.readFileFromClasspath("search.criteria.project.success.json");
    Gson gson = new Gson();
    private final SearchCriteriaDto searchCriteriaDtoSuccess = gson.fromJson(searchCriteria, SearchCriteriaDto.class);
    private final SearchCriteriaDto searchCriteriaDtoWrongColumnCase = gson.fromJson(ResourceUtil.readFileFromClasspath("search.criteria.project.wrong.column.json"), SearchCriteriaDto.class);
    private final Project expectedProject = getExpectedProject();
    private final SearchSort searchSort = new SearchSort(NAME, true);
    private final List<SearchSort> searchSortList = List.of(searchSort);

    private final SearchCriteriaDto searchCriteriaDtoFiltersNull = new SearchCriteriaDto(null, PAGE, SIZE, searchSortList);
    private final SearchCriteriaDto searchCriteriaDtoFiltersEmpty = new SearchCriteriaDto(List.of(), PAGE, SIZE, searchSortList);
    private final List<Project> expectedProjectList = List.of(expectedProject);
    private final PageRequest expectedPage = PageRequest.of(PAGE, SIZE, Sort.by(Sort.Direction.ASC, NAME));
    @Mock
    private PageableMapper pageableMapper;
    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ProjectSearchRepository projectSearchRepository;
    @InjectMocks
    private EsProjectService projectService;
    @Captor
    private ArgumentCaptor<Pageable> pageCaptor;


    @Test
    void shouldCreateProject() throws EntityAlreadyExistsException {
        //given
        when(projectRepository.findById(anyLong())).thenReturn(Optional.empty());
        //when
        projectService.createProject(expectedProject);
        //then
        verify(projectRepository).save(expectedProject);
    }

    @Test
    void shouldTrowExceptionWhenFindByIsPresent() {
        //given
        when(projectRepository.findById(anyLong())).thenReturn(Optional.of(expectedProject));
        //when
        EntityAlreadyExistsException exception = assertThrows(EntityAlreadyExistsException.class,
                () -> projectService.createProject(expectedProject));
        //then
        assertEquals("[ELASTIC] Project already exits id= 1", exception.getMessage());
    }

    @Test
    void shouldUpdateProject() {
        projectService.updateProject(expectedProject.getId(), expectedProject);
        verify(projectRepository).save(expectedProject);
    }

    @Test
    void shouldDeleteProject() {
        projectService.deleteProject(expectedProject.getId());
        verify(projectRepository).deleteById(expectedProject.getId());
    }

    @Test
    void shouldReturnAllProjects() {
        //given
        when(projectRepository.findAll()).thenReturn(List.of(expectedProject));
        //when
        List<Project> projects = projectService.findAll();
        //then
        assertEquals(expectedProjectList, projects);
    }

    @Test
    void shouldReturnAllProjectsByPageable() {
        //given
        when(projectRepository.findAll(expectedPage)).thenReturn(new PageImpl<>(expectedProjectList, expectedPage, 1));
        //when
        List<Project> projects = projectService.findAll(expectedPage);
        //then
        assertEquals(expectedProjectList, projects);
    }

    @Test
    void shouldReturnProjectsByFilters() {
        //given
        System.out.println(searchCriteriaDtoSuccess);
        when(projectSearchRepository.findAllWithFilters(searchCriteriaDtoSuccess)).thenReturn(expectedProjectList);
        //when
        List<Project> projects = projectService.findAllWithFilters(searchCriteriaDtoSuccess);
        //then
        verify(projectSearchRepository).findAllWithFilters(searchCriteriaDtoSuccess);
        assertEquals(expectedProjectList, projects);
    }

    @Test
    void shouldTrowExceptionWhenWrongColumn() {

        //when
        assertThrows(IllegalArgumentException.class, () -> projectService.findAllWithFilters(searchCriteriaDtoWrongColumnCase));
        //then
        verify(projectSearchRepository, never()).findAllWithFilters(searchCriteriaDtoWrongColumnCase);
    }

    @Test
    void shouldReturnProjectsWithNullFilters() {
        //given
        when(projectRepository.findAll(expectedPage)).thenReturn(new PageImpl<>(expectedProjectList, expectedPage, 1));
        when(pageableMapper.getPageable(searchCriteriaDtoFiltersNull)).thenCallRealMethod();
        //when
        List<Project> projects = projectService.findAllWithFilters(searchCriteriaDtoFiltersNull);

        //then
        verify(projectRepository).findAll(pageCaptor.capture());
        verify(pageableMapper).getPageable(searchCriteriaDtoFiltersNull);
        Pageable pageable = pageCaptor.getValue();
        assertEquals(expectedProjectList, projects);
        assertEquals(expectedPage, pageable);
    }

    @Test
    void shouldReturnProjectsWithEmptyFilters() {
        //given
        when(projectRepository.findAll(expectedPage)).thenReturn(new PageImpl<>(expectedProjectList, expectedPage, 1));
        when(pageableMapper.getPageable(searchCriteriaDtoFiltersEmpty)).thenCallRealMethod();
        //when
        List<Project> projects = projectService.findAllWithFilters(searchCriteriaDtoFiltersEmpty);

        //then
        verify(projectRepository).findAll(pageCaptor.capture());
        verify(pageableMapper).getPageable(searchCriteriaDtoFiltersEmpty);
        Pageable pageable = pageCaptor.getValue();
        assertEquals(expectedProjectList, projects);
        assertEquals(expectedPage, pageable);
    }


    @Test
    void shouldReturnProjectById() {
        //given
        Optional<Project> EXPECTED_OPTIONAL_PROJECT = Optional.of(expectedProject);
        when(projectRepository.findById(anyLong())).thenReturn(Optional.of(expectedProject));
        //when
        Optional<Project> project = projectService.findById(expectedProject.getId());
        //then
        verify(projectRepository).findById(expectedProject.getId());
        assertEquals(EXPECTED_OPTIONAL_PROJECT, project);

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
}