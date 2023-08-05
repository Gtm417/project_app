package org.example.projectapp.service;

import org.example.projectapp.controller.dto.VacancyDto;

public interface VacancyService {
    VacancyDto createVacancy(VacancyDto dto);

    void subscribeTo(Long vacancyId);

    void unsubscribeFrom(Long vacancyId);
}
