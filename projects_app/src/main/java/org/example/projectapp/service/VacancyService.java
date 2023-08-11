package org.example.projectapp.service;

import org.example.projectapp.controller.dto.VacancyDto;
import org.example.projectapp.model.Vacancy;

import java.util.List;

public interface VacancyService {
    VacancyDto createVacancy(VacancyDto dto);

    void subscribeTo(Long vacancyId);


    void unsubscribeFrom(Long vacancyId);

    List<Vacancy> findVacanciesByListId(List<Long> ids);

    List<Vacancy> findAllVacancies();

    VacancyDto updateVacancy(Long vacancyId, VacancyDto dto);

    void deleteVacancy(Long projectId, Long vacancyId);

}
