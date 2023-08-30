package org.example.projectapp.restclient;

import org.example.projectapp.controller.dto.VacancyDto;
import org.example.projectapp.mapper.dto.SearchElasticCriteriaDto;
import org.example.projectapp.mapper.dto.VacancyElasticDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "${service.elastic.vacancies.feign.name}", url = "${service.elastic.feign.url}/vacancies")
public interface ElasticVacanciesServiceClient {

    @GetMapping
    List<VacancyDto> getVacancies();

    @PostMapping
    void createVacancy(VacancyElasticDto vacancyElasticDto);

    @PostMapping("/sync")
    void createVacancyList(List<VacancyElasticDto> vacancyElasticDtoList);

    @PutMapping("/{id}")
    void updateVacancy(@PathVariable long id, VacancyElasticDto vacancyElasticDto);

    @DeleteMapping("/{id}")
    void deleteVacancy(@PathVariable long id);

    @PostMapping("/search")
    List<VacancyDto> searchVacancies(SearchElasticCriteriaDto criteriaDto);
}
