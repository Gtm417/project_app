package com.hodik.elastic.controller;

import com.hodik.elastic.dto.SearchCriteriaDto;
import com.hodik.elastic.dto.SearchDto;
import com.hodik.elastic.dto.VacancyDto;
import com.hodik.elastic.exception.EntityAlreadyExistsException;
import com.hodik.elastic.exception.EntityNotFoundException;
import com.hodik.elastic.mapper.VacancyMapper;
import com.hodik.elastic.model.Vacancy;
import com.hodik.elastic.service.EsVacancyService;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/vacancies")
@Log4j2
public class VacancyController {
    private final EsVacancyService vacancyService;
    private final VacancyMapper vacancyMapper;


    @Autowired
    public VacancyController(EsVacancyService vacancyService, VacancyMapper vacancyMapper) {
        this.vacancyService = vacancyService;
        this.vacancyMapper = vacancyMapper;

    }

    @PostMapping()
    public ResponseEntity<HttpStatus> createVacancy(@RequestBody VacancyDto vacancyDto) throws EntityAlreadyExistsException {
        Vacancy vacancy = vacancyMapper.convertToVacancy(vacancyDto);
        vacancyService.create(vacancy);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/sync")
    public ResponseEntity<HttpStatus> createVacanciesList(@RequestBody List<VacancyDto> vacancyDtoList) {
        List<Vacancy> vacancies = vacancyDtoList
                .stream()
                .map(vacancyMapper::convertToVacancy)
                .toList();
        vacancyService.createVacanciesList(vacancies);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_APP')")
    public ResponseEntity<HttpStatus> updateVacancy(@PathVariable long id, @RequestBody VacancyDto vacancyDto) {
        Vacancy vacancy = vacancyMapper.convertToVacancy(vacancyDto);
        vacancyService.update(id, vacancy);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_APP')")
    public ResponseEntity<HttpStatus> deleteVacancy(@PathVariable long id) {
        vacancyService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public VacancyDto getVacancy(@PathVariable long id) {
        Vacancy vacancy = vacancyService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vacancy not found with id= " + id));
        return vacancyMapper.convertToVacancyDto(vacancy);
    }

    @GetMapping()
    public List<VacancyDto> getVacancies() {
        List<Vacancy> vacancies = vacancyService.findAll();
        return getVacancyDtoList(vacancies);
    }

    private List<VacancyDto> getVacancyDtoList(List<Vacancy> vacancies) {
        return vacancies
                .stream()
                .map(vacancyMapper::convertToVacancyDto)
                .collect(Collectors.toList());
    }

    // search from main-app
    @PostMapping("/search/1")
    public List<VacancyDto> searchByCriteria(@RequestBody SearchCriteriaDto searchCriteriaDto) {
        log.info("Search request to index Vacancies" + searchCriteriaDto);
        List<Vacancy> vacancies = vacancyService.findAllWithFilters(searchCriteriaDto);
        return getVacancyDtoList(vacancies);
    }

    @PostMapping("/search")
    public ResponseEntity<List<VacancyDto>> searchVacanciesInElastic(@RequestBody @Valid SearchDto searchDto) {
        log.info("Search request to index Vacancies");
        List<Vacancy> vacancies = vacancyService.findAllWithSearch(searchDto);
        return ResponseEntity.ok(getVacancyDtoList(vacancies));
    }

}
