package com.hodik.elastic.controllers;

import com.google.gson.Gson;
import com.hodik.elastic.dto.SearchCriteriaDto;
import com.hodik.elastic.dto.VacancyDto;
import com.hodik.elastic.exceptions.EntityAlreadyExistsException;
import com.hodik.elastic.exceptions.EntityNotFoundException;
import com.hodik.elastic.mappers.VacancyMapper;
import com.hodik.elastic.model.Vacancy;
import com.hodik.elastic.services.EsVacancyService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.elasticsearch.core.ResourceUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VacancyControllerTest {
    public static final long ID = 1L;
    public static final String ABOUT_PROJECT = "about project";
    public static final String CREATOR = "Creator";
    public static final String DESCRIPTION = "Description";
    public static final String EXPECTED = "Expected";
    public static final long PROJECT_ID = 1L;
    public static final String JOB_POSITION = "job position";
    private final Vacancy expectedVacancy = getVacancyBuild();

    private final List<Vacancy> expectedVacancyList = List.of(expectedVacancy);
    private final VacancyDto expectedVacancyDto = getVacancyDtoBuild();
    private final List<VacancyDto> expectedVacancyDtoList = List.of(expectedVacancyDto);
    private final Gson gson = new Gson();

    private final SearchCriteriaDto searchCriteriaDto = gson.fromJson(ResourceUtil.readFileFromClasspath("search.criteria.vacancy.success.json"), SearchCriteriaDto.class);


    @Mock
    private EsVacancyService vacancyService;
    @Mock
    private VacancyMapper vacancyMapper;
    @InjectMocks
    private VacancyController vacancyController;

    @Test
    void createVacancySuccess() throws EntityAlreadyExistsException {
        //given
        when(vacancyMapper.convertToVacancy(expectedVacancyDto)).thenReturn(expectedVacancy);

        //when
        ResponseEntity<HttpStatus> response = vacancyController.createVacancy(expectedVacancyDto);
        //then
        verify(vacancyService).create(expectedVacancy);
        assertEquals(HttpStatus.OK, response.getStatusCode());

    }

    @Test
    void createVacancyException() throws EntityAlreadyExistsException {
        //given
        when(vacancyMapper.convertToVacancy(expectedVacancyDto)).thenReturn(expectedVacancy);
        Mockito.doThrow(EntityAlreadyExistsException.class).when(vacancyService).create(expectedVacancy);
        //when
        assertThrows(EntityAlreadyExistsException.class,
                () -> vacancyController.createVacancy(expectedVacancyDto));
        //then
        verify(vacancyService).create(expectedVacancy);


    }

    @Test
    void updateVacancy() {
        //given
        when(vacancyMapper.convertToVacancy(expectedVacancyDto)).thenReturn(expectedVacancy);
        //when
        ResponseEntity<HttpStatus> response = vacancyController.updateVacancy(expectedVacancyDto.getId(), expectedVacancyDto);
        //then
        verify(vacancyService).update(expectedVacancy.getId(), expectedVacancy);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void deleteVacancy() {
        //when
        ResponseEntity<HttpStatus> response = vacancyController.deleteVacancy(expectedVacancyDto.getId());
        //then
        verify(vacancyService).delete(expectedVacancy.getId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getVacancySuccess() {
        //given
        when(vacancyService.findById(ID)).thenReturn(Optional.of(expectedVacancy));
        when(vacancyMapper.convertToVacancyDto(expectedVacancy)).thenReturn(expectedVacancyDto);
        //when
        VacancyDto vacancyDto = vacancyController.getVacancy(ID);
        //then
        verify(vacancyService).findById(ID);
        verify(vacancyMapper).convertToVacancyDto(expectedVacancy);
        assertEquals(expectedVacancyDto, vacancyDto);
    }

    @Test
    void getVacancyException() {
        //given
        when(vacancyService.findById(ID)).thenThrow(EntityNotFoundException.class);
        //when
        assertThrows(EntityNotFoundException.class, () -> vacancyController.getVacancy(ID));
        //then
        verify(vacancyService).findById(ID);
    }

    @Test
    void getVacancies() {
        //given
        when(vacancyMapper.convertToVacancyDto(expectedVacancy)).thenReturn(expectedVacancyDto);
        when(vacancyService.findAll()).thenReturn(expectedVacancyList);
        //when
        List<VacancyDto> vacancyDtoList = vacancyController.getVacancies();
        //then
        verify(vacancyMapper).convertToVacancyDto(expectedVacancy);
        verify(vacancyService).findAll();
        assertEquals(expectedVacancyDtoList, vacancyDtoList);


    }

    @Test
    void searchByCriteria() {
        //given
        when(vacancyMapper.convertToVacancyDto(expectedVacancy)).thenReturn(expectedVacancyDto);
        when(vacancyService.findAllWithFilters(searchCriteriaDto)).thenReturn(expectedVacancyList);
        //when
        List<VacancyDto> vacancyDtoList = vacancyController.searchByCriteria(searchCriteriaDto);
        //then
        verify(vacancyMapper).convertToVacancyDto(expectedVacancy);
        verify(vacancyService).findAllWithFilters(searchCriteriaDto);
        assertEquals(expectedVacancyDtoList, vacancyDtoList);
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

    private VacancyDto getVacancyDtoBuild() {
        return VacancyDto.builder()
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