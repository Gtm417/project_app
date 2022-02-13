package org.example.projectapp.service;

import org.example.projectapp.controller.dto.VacancyDto;
import org.example.projectapp.model.Vacancy;

public interface VacancyService {
    Vacancy createVacancy(VacancyDto dto);
}
