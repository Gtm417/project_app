package com.hodik.elastic.services;

import com.hodik.elastic.exceptions.EntityAlreadyExitsException;
import com.hodik.elastic.model.Vacancy;
import com.hodik.elastic.repositories.VacancyRepository;
import org.elasticsearch.common.recycler.Recycler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EsVacancyService {
    private final VacancyRepository vacancyRepository;

    @Autowired
    public EsVacancyService(VacancyRepository vacancyRepository) {
        this.vacancyRepository = vacancyRepository;
    }

    public void create(Vacancy vacancy) throws EntityAlreadyExitsException {
        long id = vacancy.getId();
        if (vacancyRepository.findById(id).isPresent()) {
            throw new EntityAlreadyExitsException("Vacancy already exists id= " +id);
        }
        vacancyRepository.save(vacancy);
    }

    public void update(Vacancy vacancy) {
        vacancyRepository.save(vacancy);
    }

    public void delete(long id) {
    }

    public Optional<Vacancy> findById(long id) {
        return vacancyRepository.findById(id);
    }

    public List<Vacancy> findAll() {
        List<Vacancy> vacancies= new ArrayList<>();
        vacancyRepository.findAll().forEach(vacancies::add);
        return vacancies;
    }
}
