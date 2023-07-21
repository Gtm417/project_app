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
    private final SearchSort SEARCH_SORT = new SearchSort("Name", true);
    private final List<SearchSort> SEARCH_SORT_LIST = List.of(SEARCH_SORT);
    private final SearchFilter SEARCH_FILTER = new SearchFilter("Name", LIKE, List.of("Name"));
    private final SearchCriteriaDto SEARCH_CRITERIA_DTO = new SearchCriteriaDto(List.of(SEARCH_FILTER), 0, 2, SEARCH_SORT_LIST);
    private final SearchCriteriaDto SEARCH_CRITERIA_DTO_FILTERS_NULL = new SearchCriteriaDto(null, 0, 2, SEARCH_SORT_LIST);
    private final SearchCriteriaDto SEARCH_CRITERIA_DTO_FILTERS_EMPTY = new SearchCriteriaDto(List.of(), 0, 2, SEARCH_SORT_LIST);
    private final List<Project> PROJECTS = List.of(PROJECT);
    private final PageRequest EXPECTED_PAGE = PageRequest.of(0, 2, Sort.by(Sort.Direction.ASC, "Name"));
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
        projectService.createProject(PROJECT);
        //then
        verify(projectRepository).save(PROJECT);
    }

    @Test
    void createProjectException() {
        //given
        when(projectRepository.findById(anyLong())).thenReturn(Optional.of(PROJECT));
        //when
        EntityAlreadyExistsException exception = assertThrows(EntityAlreadyExistsException.class,
                () -> projectService.createProject(PROJECT));
        //then
        assertEquals("Project already exits id= 1", exception.getMessage());
    }

    @Test
    void updateProject() {
        projectService.updateProject(PROJECT.getId(), PROJECT);
        verify(projectRepository).save(PROJECT);
    }

    @Test
    void deleteProject() {
        projectService.deleteProject(PROJECT.getId());
        verify(projectRepository).deleteById(PROJECT.getId());
    }

    @Test
    void findAll() {
        //given
        when(projectRepository.findAll()).thenReturn(List.of(PROJECT));
        //when
        List<Project> projects = projectService.findAll();
        //then
        assertEquals(PROJECTS, projects);
    }

    @Test
    void testFindAllPageable() {
        //given
        when(projectRepository.findAll(EXPECTED_PAGE)).thenReturn(new PageImpl<>(PROJECTS, EXPECTED_PAGE, 1));
        //when
        List<Project> projects = projectService.findAll(EXPECTED_PAGE);
        //then
        assertEquals(PROJECTS, projects);
    }

    @Test
    void findAllWithFilters() {
        //given
        when(projectSearchRepository.findAllWithFilters(SEARCH_CRITERIA_DTO)).thenReturn(PROJECTS);
        //when
        List<Project> projects = projectService.findAllWithFilters(SEARCH_CRITERIA_DTO);
        //then
        verify(projectSearchRepository).findAllWithFilters(SEARCH_CRITERIA_DTO);
        assertEquals(PROJECTS, projects);
    }

    @Test
    void findAllWithNullFilters() {
        //given
        when(projectRepository.findAll(EXPECTED_PAGE)).thenReturn(new PageImpl<>(PROJECTS, EXPECTED_PAGE, 1));
        when(pageableMapper.getPageable(SEARCH_CRITERIA_DTO_FILTERS_NULL)).thenCallRealMethod();
        //when
        List<Project> projects = projectService.findAllWithFilters(SEARCH_CRITERIA_DTO_FILTERS_NULL);

        //then
        verify(projectRepository).findAll(pageCaptor.capture());
        verify(pageableMapper).getPageable(SEARCH_CRITERIA_DTO_FILTERS_NULL);
        Pageable pageable = pageCaptor.getValue();
        assertEquals(PROJECTS, projects);
        assertEquals(EXPECTED_PAGE, pageable);
    }

    @Test
    void findAllWithEmptyFilters() {
        //given
        when(projectRepository.findAll(EXPECTED_PAGE)).thenReturn(new PageImpl<>(PROJECTS, EXPECTED_PAGE, 1));
        when(pageableMapper.getPageable(SEARCH_CRITERIA_DTO_FILTERS_EMPTY)).thenCallRealMethod();
        //when
        List<Project> projects = projectService.findAllWithFilters(SEARCH_CRITERIA_DTO_FILTERS_EMPTY);

        //then
        verify(projectRepository).findAll(pageCaptor.capture());
        verify(pageableMapper).getPageable(SEARCH_CRITERIA_DTO_FILTERS_EMPTY);
        Pageable pageable = pageCaptor.getValue();
        assertEquals(PROJECTS, projects);
        assertEquals(EXPECTED_PAGE, pageable);
    }


    @Test
    void findById() {
        //given
        Optional<Project> EXPECTED_OPTIONAL_PROJECT = Optional.of(PROJECT);
        when(projectRepository.findById(anyLong())).thenReturn(Optional.of(PROJECT));
        //when
        Optional<Project> project = projectService.findById(PROJECT.getId());
        //then
        verify(projectRepository).findById(PROJECT.getId());
        assertEquals(EXPECTED_OPTIONAL_PROJECT,project);

    }
}