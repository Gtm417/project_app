package com.hodik.elastic.services;

import com.google.gson.Gson;
import com.hodik.elastic.dto.SearchCriteriaDto;
import com.hodik.elastic.dto.SearchSort;
import com.hodik.elastic.exceptions.EntityAlreadyExistsException;
import com.hodik.elastic.mappers.PageableMapper;
import com.hodik.elastic.model.Vacancy;
import com.hodik.elastic.repositories.VacancyRepository;
import com.hodik.elastic.repositories.VacancySearchRepository;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EsVacancyServiceTest {
    public static final long ID = 1L;
    public static final String ABOUT_PROJECT = "about project";
    public static final String CREATOR = "Creator";
    public static final String DESCRIPTION = "Description";
    public static final String EXPECTED = "Expected";
    public static final long PROJECT_ID = 1L;
    public static final String JOB_POSITION = "job position";
    private final Vacancy expectedVacancy = getVacancyBuild();
    private final List<Vacancy> expectedVacancyList = List.of(expectedVacancy);
    private final SearchSort searchSort = new SearchSort("Creator", true);
    private final List<SearchSort> searchSortList = List.of(searchSort);

    private final Gson gson= new Gson();
    private final SearchCriteriaDto searchCriteriaDtoSuccess = gson.fromJson(ResourceUtil.readFileFromClasspath("search.criteria.vacancy.success.json"), SearchCriteriaDto.class);
    private final SearchCriteriaDto searchCriteriaDtoWrong = gson.fromJson(ResourceUtil.readFileFromClasspath("search.criteria.vacancy.wrong.json"), SearchCriteriaDto.class);
    private final SearchCriteriaDto searchCriteriaDtoFiltersNull = new SearchCriteriaDto(null, 0, 2, searchSortList);
    private final SearchCriteriaDto searchCriteriaDtoFiltersEmpty = new SearchCriteriaDto(List.of(), 0, 2, searchSortList);

    private final PageRequest expectedPage = PageRequest.of(0, 2, Sort.by(Sort.Direction.ASC, "Creator"));

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
    void createSuccess() throws EntityAlreadyExistsException {
        //given
        when(vacancyRepository.findById(anyLong())).thenReturn(Optional.empty());
        //when
        vacancyService.create(expectedVacancy);
        //then
        verify(vacancyRepository).save(expectedVacancy);
        verify(vacancyRepository).findById(expectedVacancy.getId());
    }

    @Test
    void createException() {
        //given
        when(vacancyRepository.findById(anyLong())).thenReturn(Optional.of(expectedVacancy));
        //when
        EntityAlreadyExistsException exception = assertThrows(EntityAlreadyExistsException.class, () -> vacancyService.create(expectedVacancy));
        //then
        verify(vacancyRepository).findById(expectedVacancy.getId());
        assertEquals("Vacancy already exists id= 1", exception.getMessage());
    }

    @Test
    void update() {
        vacancyService.update(expectedVacancy.getId(), expectedVacancy);
        verify(vacancyRepository).save(expectedVacancy);
    }

    @Test
    void delete() {
        vacancyService.delete(ID);
        verify(vacancyRepository).deleteById(expectedVacancy.getId());
    }

    @Test
    void findById() {
        //given
        when(vacancyRepository.findById(anyLong())).thenReturn(Optional.of(expectedVacancy));
        //when
        Optional<Vacancy> vacancy = vacancyService.findById(expectedVacancy.getId());
        //then
        assertEquals(Optional.of(expectedVacancy), vacancy);
        verify(vacancyRepository).findById(expectedVacancy.getId());


    }

    @Test
    void findAll() {
        //given
        when(vacancyRepository.findAll()).thenReturn(expectedVacancyList);
        //when
        List<Vacancy> vacancies = vacancyService.findAll();
        //then
        verify(vacancyRepository).findAll();
        assertEquals(expectedVacancyList, vacancies);
    }

    @Test
    void FindAllPageable() {
        //given
        when(vacancyRepository.findAll(expectedPage)).thenReturn(new PageImpl<>(expectedVacancyList, expectedPage, 1));
        //when
        List<Vacancy> vacancies = vacancyService.findAll(expectedPage);
        //then
        assertEquals(expectedVacancyList, vacancies);
        verify(vacancyRepository).findAll(expectedPage);

    }

    @Test
    void findAllWithFilters() {
        //given
        when(vacancySearchRepository.findAllWithFilters(any())).thenReturn(expectedVacancyList);
        //when
        List<Vacancy> vacancies = vacancyService.findAllWithFilters(searchCriteriaDtoSuccess);
        //then
        assertEquals(expectedVacancyList, vacancies);
        verify(vacancySearchRepository).findAllWithFilters(searchCriteriaDtoSuccess);
    }
    @Test
    void findAllWithException() {

        //when
       assertThrows(IllegalArgumentException.class, ()->vacancyService.findAllWithFilters(searchCriteriaDtoWrong));
        //then

        verify(vacancySearchRepository, never()).findAllWithFilters(searchCriteriaDtoWrong);
    }

    @Test
    void findAllWithNullFilters() {
        //given
        when(vacancyRepository.findAll(expectedPage)).thenReturn(new PageImpl<>(expectedVacancyList, expectedPage, 1));
        when(pageableMapper.getPageable(searchCriteriaDtoFiltersNull)).thenCallRealMethod();
        //when
        List<Vacancy> vacancies = vacancyService.findAllWithFilters(searchCriteriaDtoFiltersNull);
        //then
        assertEquals(expectedVacancyList, vacancies);
        verify(vacancyRepository).findAll(pageableCaptor.capture());
        verify(pageableMapper).getPageable(searchCriteriaDtoFiltersNull);
        Pageable value = pageableCaptor.getValue();
        assertEquals(expectedPage, value);
    }

    @Test
    void findAllWithEmptyFilters() {
        //given
        when(vacancyRepository.findAll(expectedPage)).thenReturn(new PageImpl<>(expectedVacancyList, expectedPage, 1));
        when(pageableMapper.getPageable(searchCriteriaDtoFiltersEmpty)).thenCallRealMethod();
        //when
        List<Vacancy> vacancies = vacancyService.findAllWithFilters(searchCriteriaDtoFiltersEmpty);
        //then
        assertEquals(expectedVacancyList, vacancies);
        verify(pageableMapper).getPageable(searchCriteriaDtoFiltersEmpty);
        verify(vacancyRepository).findAll(pageableCaptor.capture());
        Pageable value = pageableCaptor.getValue();
        assertEquals(expectedPage, value);
    }

    private Vacancy getVacancyBuild() {
        return Vacancy.builder()
                .id(ID)
                .aboutProject(ABOUT_PROJECT)
                .creator(CREATOR)
                .description(DESCRIPTION)
                .expected(EXPECTED)
                .projectId(PROJECT_ID)
                .jobPosition(JOB_POSITION)
                .build();
    }
}