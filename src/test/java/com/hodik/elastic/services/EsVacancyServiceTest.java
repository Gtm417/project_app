package com.hodik.elastic.services;

import com.hodik.elastic.dto.SearchCriteriaDto;
import com.hodik.elastic.dto.SearchFilter;
import com.hodik.elastic.dto.SearchSort;
import com.hodik.elastic.exceptions.EntityAlreadyExitsException;
import com.hodik.elastic.mappers.PageableMapper;
import com.hodik.elastic.model.Project;
import com.hodik.elastic.model.Vacancy;
import com.hodik.elastic.repositories.VacancyRepository;
import com.hodik.elastic.repositories.VacancySearchRepository;
import jdk.dynalink.linker.LinkerServices;
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

import java.util.List;
import java.util.Optional;

import static com.hodik.elastic.util.Operations.LIKE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EsVacancyServiceTest {
    private final Vacancy VACANCY = Vacancy.builder()
            .id(1L)
            .aboutProject("about project")
            .creator("Creator")
            .description("Description")
            .expected("Expected")
            .projectId(1L)
            .jobPosition("job position")
            .build();
    private final List<Vacancy> VACANCIES = List.of(VACANCY);
    private final SearchSort SEARCH_SORT = new SearchSort("Creator", true);
    private final List<SearchSort> SEARCH_SORT_LIST = List.of(SEARCH_SORT);
    private final SearchFilter SEARCH_FILTER = new SearchFilter("Creator", LIKE, List.of("Creator"));
    private final SearchCriteriaDto SEARCH_CRITERIA_DTO = new SearchCriteriaDto(List.of(SEARCH_FILTER), 0, 2, SEARCH_SORT_LIST);
    private final SearchCriteriaDto SEARCH_CRITERIA_DTO_FILTERS_NULL = new SearchCriteriaDto(null, 0, 2, SEARCH_SORT_LIST);
    private final SearchCriteriaDto SEARCH_CRITERIA_DTO_FILTERS_EMPTY = new SearchCriteriaDto(List.of(), 0, 2, SEARCH_SORT_LIST);

    private final PageRequest EXPECTED_PAGE = PageRequest.of(0, 2, Sort.by(Sort.Direction.ASC, "Creator"));

    @Mock
    private VacancyRepository vacancyRepository;
    @Mock
    private VacancySearchRepository vacancySearchRepository;
    @Mock
    private PageableMapper pageableMapper;
    @InjectMocks
    private EsVacancyService vacancyService;
    @Captor
    private ArgumentCaptor<Pageable> pageableCaptor;

    @Test
    void createSuccess() throws EntityAlreadyExitsException {
        //given
        when(vacancyRepository.findById(anyLong())).thenReturn(Optional.empty());
        //when
        vacancyService.create(VACANCY);
        //then
        verify(vacancyRepository).save(VACANCY);
        verify(vacancyRepository).findById(VACANCY.getId());
    }
    @Test
    void createException() throws EntityAlreadyExitsException {
        //given
        when(vacancyRepository.findById(anyLong())).thenReturn(Optional.of(VACANCY));
        //when
       EntityAlreadyExitsException exception= assertThrows(EntityAlreadyExitsException.class, ()-> vacancyService.create(VACANCY));
        //then
        verify(vacancyRepository).findById(VACANCY.getId());
        assertEquals("Vacancy already exists id= 1", exception.getMessage());
    }

    @Test
    void update() {
        vacancyService.update(VACANCY.getId(), VACANCY);
        verify(vacancyRepository).save(VACANCY);
    }

    @Test
    void delete() {
        vacancyService.delete(VACANCY.getId());
        verify(vacancyRepository).deleteById(VACANCY.getId());
    }

    @Test
    void findById() {
        //given
        when(vacancyRepository.findById(anyLong())).thenReturn(Optional.of(VACANCY));
        //when
        Optional<Vacancy> vacancy= vacancyService.findById(VACANCY.getId());
        //then
        assertEquals(Optional.of(VACANCY), vacancy);
        verify(vacancyRepository).findById(VACANCY.getId());


    }

    @Test
    void findAll() {
        //given
        when(vacancyRepository.findAll()).thenReturn(VACANCIES);
        //when
        List<Vacancy> vacancies = vacancyService.findAll();
        //then
        verify(vacancyRepository).findAll();
        assertEquals(VACANCIES, vacancies);
    }

    @Test
    void FindAllPageable() {
        //given
        when(vacancyRepository.findAll(EXPECTED_PAGE)).thenReturn(new PageImpl<>(VACANCIES, EXPECTED_PAGE,1));
        //when
        List<Vacancy> vacancies = vacancyService.findAll(EXPECTED_PAGE);
        //then
        assertEquals(VACANCIES, vacancies);
        verify(vacancyRepository).findAll(EXPECTED_PAGE);

    }

    @Test
    void findAllWithFilters() {
        //given
        when(vacancySearchRepository.findAllWithFilters(any())).thenReturn(VACANCIES);
        //when
        List<Vacancy> vacancies = vacancyService.findAllWithFilters(SEARCH_CRITERIA_DTO);
        //then
        assertEquals(VACANCIES, vacancies);
        verify(vacancySearchRepository).findAllWithFilters(SEARCH_CRITERIA_DTO);
    }
    @Test
    void findAllWithNullFilters() {
        //given
        when(vacancyRepository.findAll(EXPECTED_PAGE)).thenReturn(new PageImpl<>(VACANCIES, EXPECTED_PAGE,1));
        when(pageableMapper.getPageable(SEARCH_CRITERIA_DTO_FILTERS_NULL)).thenCallRealMethod();
        //when
        List<Vacancy> vacancies = vacancyService.findAllWithFilters(SEARCH_CRITERIA_DTO_FILTERS_NULL);
        //then
        assertEquals(VACANCIES, vacancies);
        verify(vacancyRepository).findAll(pageableCaptor.capture());
        verify(pageableMapper).getPageable(SEARCH_CRITERIA_DTO_FILTERS_NULL);
        Pageable value= pageableCaptor.getValue();
        assertEquals(EXPECTED_PAGE, value);
    }
    @Test
    void findAllWithEmptyFilters() {
        //given
        when(vacancyRepository.findAll(EXPECTED_PAGE)).thenReturn(new PageImpl<>(VACANCIES, EXPECTED_PAGE,1));
        when(pageableMapper.getPageable(SEARCH_CRITERIA_DTO_FILTERS_EMPTY)).thenCallRealMethod();
        //when
        List<Vacancy> vacancies = vacancyService.findAllWithFilters(SEARCH_CRITERIA_DTO_FILTERS_EMPTY);
        //then
        assertEquals(VACANCIES, vacancies);
        verify(pageableMapper).getPageable(SEARCH_CRITERIA_DTO_FILTERS_EMPTY);
        verify(vacancyRepository).findAll(pageableCaptor.capture());
        Pageable value= pageableCaptor.getValue();
        assertEquals(EXPECTED_PAGE, value);
    }
}