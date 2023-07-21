package com.hodik.elastic.services;

import com.hodik.elastic.dto.SearchCriteriaDto;
import com.hodik.elastic.dto.SearchFilter;
import com.hodik.elastic.dto.SearchSort;
import com.hodik.elastic.exceptions.EntityAlreadyExistsException;
import com.hodik.elastic.mappers.PageableMapper;
import com.hodik.elastic.model.Project;
import com.hodik.elastic.repositories.ProjectRepository;
import com.hodik.elastic.repositories.ProjectSearchRepository;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.hodik.elastic.util.Operations.LIKE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EsProjectServiceTest {
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
    private final SearchSort searchSort = new SearchSort("Name", true);
    private final List<SearchSort> searchSortList = List.of(searchSort);
    private final SearchFilter searchFilter = new SearchFilter("Name", LIKE, List.of("Name"));
    private final SearchCriteriaDto searchCriteriaDto = new SearchCriteriaDto(List.of(searchFilter), 0, 2, searchSortList);
    private final SearchCriteriaDto searchCriteriaDtoFiltersNull = new SearchCriteriaDto(null, 0, 2, searchSortList);
    private final SearchCriteriaDto searchCriteriaDtoFiltersEmpty = new SearchCriteriaDto(List.of(), 0, 2, searchSortList);
    private final List<Project> expectedProjectList = List.of(expectedProject);
    private final PageRequest expectedPage = PageRequest.of(0, 2, Sort.by(Sort.Direction.ASC, "Name"));
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
    void createProjectSuccess() throws EntityAlreadyExistsException {
        //given
        when(projectRepository.findById(anyLong())).thenReturn(Optional.empty());
        //when
        projectService.createProject(expectedProject);
        //then
        verify(projectRepository).save(expectedProject);
    }

    @Test
    void createProjectException() {
        //given
        when(projectRepository.findById(anyLong())).thenReturn(Optional.of(expectedProject));
        //when
        EntityAlreadyExistsException exception = assertThrows(EntityAlreadyExistsException.class,
                () -> projectService.createProject(expectedProject));
        //then
        assertEquals("Project already exits id= 1", exception.getMessage());
    }

    @Test
    void updateProject() {
        projectService.updateProject(expectedProject.getId(), expectedProject);
        verify(projectRepository).save(expectedProject);
    }

    @Test
    void deleteProject() {
        projectService.deleteProject(expectedProject.getId());
        verify(projectRepository).deleteById(expectedProject.getId());
    }

    @Test
    void findAll() {
        //given
        when(projectRepository.findAll()).thenReturn(List.of(expectedProject));
        //when
        List<Project> projects = projectService.findAll();
        //then
        assertEquals(expectedProjectList, projects);
    }

    @Test
    void testFindAllPageable() {
        //given
        when(projectRepository.findAll(expectedPage)).thenReturn(new PageImpl<>(expectedProjectList, expectedPage, 1));
        //when
        List<Project> projects = projectService.findAll(expectedPage);
        //then
        assertEquals(expectedProjectList, projects);
    }

    @Test
    void findAllWithFilters() {
        //given
        when(projectSearchRepository.findAllWithFilters(searchCriteriaDto)).thenReturn(expectedProjectList);
        //when
        List<Project> projects = projectService.findAllWithFilters(searchCriteriaDto);
        //then
        verify(projectSearchRepository).findAllWithFilters(searchCriteriaDto);
        assertEquals(expectedProjectList, projects);
    }

    @Test
    void findAllWithNullFilters() {
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
    void findAllWithEmptyFilters() {
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
    void findById() {
        //given
        Optional<Project> EXPECTED_OPTIONAL_PROJECT = Optional.of(expectedProject);
        when(projectRepository.findById(anyLong())).thenReturn(Optional.of(expectedProject));
        //when
        Optional<Project> project = projectService.findById(expectedProject.getId());
        //then
        verify(projectRepository).findById(expectedProject.getId());
        assertEquals(EXPECTED_OPTIONAL_PROJECT,project);

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