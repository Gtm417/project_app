package com.hodik.elastic.controller;

import com.hodik.elastic.dto.SearchCriteriaDto;
import com.hodik.elastic.dto.VacancyDto;
import com.hodik.elastic.exception.EntityAlreadyExistsException;
import com.hodik.elastic.exception.EntityNotFoundException;
import com.hodik.elastic.exception.VacancyErrorResponse;
import com.hodik.elastic.mapper.VacancyMapper;
import com.hodik.elastic.service.EsVacancyService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        vacancyService.create(vacancyMapper.convertToVacancy(vacancyDto));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/sync")
    public ResponseEntity<HttpStatus> createVacanciesList(@RequestBody List<VacancyDto> vacancyDtoList) {
        vacancyService.createVacanciesList(vacancyDtoList.stream().map(vacancyMapper::convertToVacancy).toList());
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> updateVacancy(@PathVariable long id, @RequestBody VacancyDto vacancyDto) {
        vacancyService.update(id, vacancyMapper.convertToVacancy(vacancyDto));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteVacancy(@PathVariable long id) {
        vacancyService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public VacancyDto getVacancy(@PathVariable long id) {
        return vacancyMapper.convertToVacancyDto(vacancyService.findById(id).orElseThrow(EntityNotFoundException::new));
    }

    @GetMapping()
    public List<VacancyDto> getVacancies() {
        return vacancyService.findAll().stream().map(vacancyMapper::convertToVacancyDto).collect(Collectors.toList());
    }

    @PostMapping("/search")
    public List<VacancyDto> searchByCriteria(@RequestBody SearchCriteriaDto searchCriteriaDto) {
        log.info("Search request to index Vacancies" + searchCriteriaDto);
        return vacancyService.findAllWithFilters(searchCriteriaDto).stream().map(vacancyMapper::convertToVacancyDto).collect(Collectors.toList());
    }

    @ExceptionHandler
    private ResponseEntity<VacancyErrorResponse> exceptionHandler(EntityAlreadyExistsException e) {
        VacancyErrorResponse message = new VacancyErrorResponse(e.getMessage());
        log.error(e.getMessage());
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<VacancyErrorResponse> exceptionHandler(EntityNotFoundException e) {
        VacancyErrorResponse message = new VacancyErrorResponse(e.getMessage());
        log.error(e.getMessage());
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<VacancyErrorResponse> exceptionHandler(IllegalArgumentException e) {
        VacancyErrorResponse message = new VacancyErrorResponse(e.getMessage());
        log.error(e.getMessage());
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }
}
