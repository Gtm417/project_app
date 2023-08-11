package org.example.projectapp.restclient;

import org.example.projectapp.controller.dto.VacancyDto;
import org.example.projectapp.mapper.dto.VacancyElasticDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "${service.elastic.vacancies.feign.name}", url = "${service.elastic.feign.url}")
public interface ElasticVacanciesServiceClient {

    @GetMapping("/vacancies")
    List<VacancyDto> getVacancies();

    @PostMapping("/vacancies")
    void createVacancy(VacancyElasticDto vacancyElasticDto);

    @PostMapping("/vacancies/sync")
    void createVacancyList(List<VacancyElasticDto> vacancyElasticDtoList);

    @PutMapping("/vacancies/{id}")
    void updateVacancy(@PathVariable long id, VacancyElasticDto vacancyElasticDto);

    @DeleteMapping("vacancies/{id}")
    void deleteVacancy(@PathVariable long id);
}
