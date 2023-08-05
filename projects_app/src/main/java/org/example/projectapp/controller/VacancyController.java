package org.example.projectapp.controller;

import org.example.projectapp.controller.dto.VacancyDto;
import org.example.projectapp.model.Vacancy;
import org.example.projectapp.service.VacancyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("vacancies")
public class VacancyController {
    private final Logger logger = LoggerFactory.getLogger(VacancyController.class);

    private final VacancyService vacancyService;

    public VacancyController(VacancyService vacancyService) {
        this.vacancyService = vacancyService;
    }

    @PostMapping
    public ResponseEntity<VacancyDto> createVacancy(@RequestBody @Valid VacancyDto dto) {
        VacancyDto vacancy = vacancyService.createVacancy(dto);
        return ResponseEntity.ok(vacancy);
    }

    @PostMapping("/{id}/subscribe")
    public ResponseEntity<?> subscribe(@PathVariable("id") Long id) {
        vacancyService.subscribeTo(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/unsubscribe")
    public ResponseEntity<Vacancy> unsubscribe(@PathVariable("id") Long id) {
        vacancyService.unsubscribeFrom(id);
        return ResponseEntity.ok().build();
    }
}
