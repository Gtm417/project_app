package org.example.projectapp.controller;

import org.example.projectapp.mapper.VacancyMapper;
import org.example.projectapp.mapper.dto.VacancyElasticDto;
import org.example.projectapp.model.Vacancy;
import org.example.projectapp.restclient.ElasticVacanciesServiceClient;
import org.example.projectapp.service.VacancyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/vacancies")
public class SynchronizeVacanciesWithElasticController {
    private final VacancyService vacancyService;
    private final ElasticVacanciesServiceClient elasticVacanciesServiceClient;
    private final VacancyMapper vacancyMapper;

    public SynchronizeVacanciesWithElasticController(VacancyService vacancyService, ElasticVacanciesServiceClient elasticVacanciesServiceClient, VacancyMapper vacancyMapper) {
        this.vacancyService = vacancyService;
        this.elasticVacanciesServiceClient = elasticVacanciesServiceClient;
        this.vacancyMapper = vacancyMapper;
    }


    @PostMapping("/sync")
    public ResponseEntity<HttpStatus> syncVacancies(@RequestBody @Nullable List<Long> ids) {
        List<Vacancy> vacancies;
        if (!CollectionUtils.isEmpty(ids)) {
            vacancies = vacancyService.findVacanciesByListId(ids);
        } else {
            vacancies = vacancyService.findAllVacancies();
        }
        List<VacancyElasticDto> vacancyElasticDtoList = vacancies.stream()
                .map(vacancyMapper::convertToVacancyElasticDto)
                .collect(Collectors.toList());
        elasticVacanciesServiceClient.createVacancyList(vacancyElasticDtoList);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
