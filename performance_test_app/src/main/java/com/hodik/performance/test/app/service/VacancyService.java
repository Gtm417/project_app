package com.hodik.performance.test.app.service;

import com.hodik.performance.test.app.model.Vacancy;
import com.hodik.performance.test.app.repository.VacancyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class VacancyService {
    public VacancyService(VacancyRepository repository) {
        this.repository = repository;
    }

    @Autowired
    private final VacancyRepository repository;

    public void saveVacanciesList(List<Vacancy> vacancies) {
        repository.saveAll(vacancies);
    }
}
