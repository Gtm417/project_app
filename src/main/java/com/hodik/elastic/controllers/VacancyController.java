package com.hodik.elastic.controllers;

import com.hodik.elastic.dto.VacancyDto;
import com.hodik.elastic.exceptions.EntityAlreadyExitsException;
import com.hodik.elastic.exceptions.EntityNotFoundException;
import com.hodik.elastic.exceptions.VacancyErrorResponse;
import com.hodik.elastic.mappers.VacancyMapper;
import com.hodik.elastic.services.EsVacancyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/vacancy")
public class VacancyController {
    private final EsVacancyService vacancyService;
    private final VacancyMapper vacancyMapper;

    @Autowired
    public VacancyController(EsVacancyService vacancyService, VacancyMapper vacancyMapper) {
        this.vacancyService = vacancyService;
        this.vacancyMapper = vacancyMapper;
    }

    @PostMapping()
    public ResponseEntity<HttpStatus> createVacancy(@RequestBody VacancyDto vacancyDto) throws EntityAlreadyExitsException {
        vacancyService.create(vacancyMapper.convertToVacancy(vacancyDto));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> updateVacancy(@RequestBody VacancyDto vacancyDto) {
        vacancyService.update(vacancyMapper.convertToVacancy(vacancyDto));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteVacancy(@PathVariable long id) {
        vacancyService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public VacancyDto getVacancy(@PathVariable long id) throws EntityAlreadyExitsException {
        return vacancyMapper.convertToVacancyDto(vacancyService.findById(id).orElseThrow(EntityNotFoundException::new));
    }

    @GetMapping()
    public List<VacancyDto> getVacancies() {
        return vacancyService.findAll().stream().map(vacancyMapper::convertToVacancyDto).collect(Collectors.toList());

    }

    @ExceptionHandler
    private ResponseEntity<VacancyErrorResponse> exceptionHandler(EntityAlreadyExitsException e) {
        VacancyErrorResponse message = new VacancyErrorResponse(e.getMessage());
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<VacancyErrorResponse> exceptionHandler(EntityNotFoundException e) {
        VacancyErrorResponse message = new VacancyErrorResponse(e.getMessage());
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }
}
