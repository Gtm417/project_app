package org.example.projectapp.controller;

import io.micrometer.core.annotation.Timed;
import org.example.projectapp.controller.dto.SearchDto;
import org.example.projectapp.controller.dto.VacancyDto;
import org.example.projectapp.model.Vacancy;
import org.example.projectapp.restclient.ElasticVacanciesServiceClient;
import org.example.projectapp.service.VacancyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("vacancies")
@Timed("main-app.vacancies")
public class VacancyController {
    private final Logger logger = LoggerFactory.getLogger(VacancyController.class);

    private final VacancyService vacancyService;
    private final ElasticVacanciesServiceClient elasticClient;

    public VacancyController(VacancyService vacancyService, ElasticVacanciesServiceClient elasticClient) {
        this.vacancyService = vacancyService;
        this.elasticClient = elasticClient;
    }

    @PostMapping
    public ResponseEntity<VacancyDto> createVacancy(@RequestBody @Valid VacancyDto dto) {
        VacancyDto vacancy = vacancyService.createVacancy(dto);
        return ResponseEntity.ok(vacancy);
    }

    @GetMapping
    public ResponseEntity<List<VacancyDto>> getVacancies() {
        return ResponseEntity.ok(elasticClient.getVacancies());
    }

    @PutMapping("/{id}")
    public ResponseEntity<VacancyDto> updateVacancy(@PathVariable("id") Long id, @RequestBody @Valid VacancyDto dto) {
        VacancyDto vacancyDto = vacancyService.updateVacancy(id, dto);
        return ResponseEntity.ok(vacancyDto);
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

    @PostMapping("/search/1")
    public ResponseEntity<List<VacancyDto>> searchVacanciesInElastic(@RequestBody SearchDto searchDto) {
        List<VacancyDto> vacanciesFromElastic = vacancyService.findVacanciesInElastic(searchDto);
        return ResponseEntity.ok(vacanciesFromElastic);
    }


}
