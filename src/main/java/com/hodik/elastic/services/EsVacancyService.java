package com.hodik.elastic.services;

import com.hodik.elastic.dto.SearchCriteriaDto;
import com.hodik.elastic.dto.SearchFilter;
import com.hodik.elastic.exceptions.EntityAlreadyExitsException;
import com.hodik.elastic.mappers.PageableMapper;
import com.hodik.elastic.model.Vacancy;
import com.hodik.elastic.repositories.VacancyRepository;
import com.hodik.elastic.repositories.VacancySearchRepository;
import com.hodik.elastic.util.SearchColumnVacancy;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class EsVacancyService {
    private final VacancyRepository vacancyRepository;
    private final VacancySearchRepository vacancySearchRepository;
    private final PageableMapper pageableMapper;

    @Autowired
    public EsVacancyService(VacancyRepository vacancyRepository, VacancySearchRepository vacancySearchRepository, PageableMapper pageableMapper) {
        this.vacancyRepository = vacancyRepository;
        this.vacancySearchRepository = vacancySearchRepository;
        this.pageableMapper = pageableMapper;
    }

    public void create(Vacancy vacancy) throws EntityAlreadyExitsException {
        long id = vacancy.getId();
        if (vacancyRepository.findById(id).isPresent()) {
            throw new EntityAlreadyExitsException("Vacancy already exists id= " + id);
        }
        vacancyRepository.save(vacancy);
        log.info("Vacancy is saved to ES successful id = " + id);
    }

    public void update(long id, Vacancy vacancy) {
        vacancy.setId(id);
        vacancyRepository.save(vacancy);
        log.info("Vacancy is saved to ES successful id = " + id);

    }

    public void delete(long id) {
        vacancyRepository.deleteById(id);
        log.info("Vacancy is deleted from ES successful id = " + id);

    }

    public Optional<Vacancy> findById(long id) {
        return vacancyRepository.findById(id);
    }

    public List<Vacancy> findAll() {
        List<Vacancy> vacancies = new ArrayList<>();
        vacancyRepository.findAll().forEach(vacancies::add);
        return vacancies;
    }

    public List<Vacancy> findAll(Pageable pageable) {
        List<Vacancy> vacancies = new ArrayList<>();
        vacancyRepository.findAll(pageable).forEach(vacancies::add);
        return vacancies;
    }

    public List<Vacancy> findAllWithFilters(SearchCriteriaDto searchCriteriaDto) {
        List<SearchFilter> filters = searchCriteriaDto.getFilters();
        if (CollectionUtils.isEmpty(filters)) {
            return findAll(pageableMapper.getPageable(searchCriteriaDto));
        }
        searchCriteriaDto.getFilters().stream().forEach(x -> SearchColumnVacancy.getByNameIgnoringCase(x.getColumn()));
        return vacancySearchRepository.findAllWithFilters(searchCriteriaDto);
    }
}
