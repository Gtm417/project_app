package com.hodik.elastic.service;

import com.hodik.elastic.dto.FilterDto;
import com.hodik.elastic.dto.SearchCriteriaDto;
import com.hodik.elastic.dto.SearchDto;
import com.hodik.elastic.exception.EntityAlreadyExistsException;
import com.hodik.elastic.mapper.PageableMapper;
import com.hodik.elastic.mapper.SearchCriteriaDtoMapper;
import com.hodik.elastic.model.User;
import com.hodik.elastic.model.Vacancy;
import com.hodik.elastic.repository.VacancyRepository;
import com.hodik.elastic.repository.VacancySearchRepository;
import com.hodik.elastic.util.SearchColumnVacancy;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    private final SearchCriteriaDtoMapper searchCriteriaDtoMapper;
    private Timer timer;
    private final MeterRegistry simpleMeterRegistry;

    @Autowired
    public EsVacancyService(VacancyRepository vacancyRepository,
                            VacancySearchRepository vacancySearchRepository,
                            PageableMapper pageableMapper, @Qualifier("searchCriteriaDtoMapper") SearchCriteriaDtoMapper searchCriteriaDtoMapper,
                            MeterRegistry simpleMeterRegistry) {
        this.vacancyRepository = vacancyRepository;
        this.vacancySearchRepository = vacancySearchRepository;
        this.pageableMapper = pageableMapper;
        this.searchCriteriaDtoMapper = searchCriteriaDtoMapper;
        this.simpleMeterRegistry = simpleMeterRegistry;
        timer = simpleMeterRegistry.timer("elastic.vacancies.search.timer");
    }

    public void create(Vacancy vacancy) throws EntityAlreadyExistsException {
        long id = vacancy.getId();
        if (vacancyRepository.findById(id).isPresent()) {
            log.error("[ELASTIC] Vacancy isn't saved. Vacancy already exists id= {}", id);
            throw new EntityAlreadyExistsException("[ELASTIC] Vacancy already exists id= " + id);
        }
        vacancyRepository.save(vacancy);
        log.info("[ELASTIC] Vacancy is saved to ES successful id = {} ", id);
    }

    public void update(long id, Vacancy vacancy) {
        vacancy.setId(id);
        vacancyRepository.save(vacancy);
        log.info("[ELASTIC] Vacancy is saved to ES successful id ={} ", id);

    }

    public void delete(long id) {
        vacancyRepository.deleteById(id);
        log.info("[ELASTIC] Vacancy is deleted from ES successful id ={} ", id);

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
        List<FilterDto> filters = searchCriteriaDto.getFilters();
        if (CollectionUtils.isEmpty(filters)) {
            return findAll(pageableMapper.getPageable(searchCriteriaDto));
        }
        validateColumnName(filters);
        Timer.Sample sample = Timer.start();
        long startTime = System.currentTimeMillis();
        List<Vacancy> allWithFilters = vacancySearchRepository.findAllWithFilters(searchCriteriaDto);
        long endTime = System.currentTimeMillis();
        double responseTimeInMilliSeconds = timer.record(() -> sample.stop(timer) / 1000000);
        log.info("Vacancies search found {} time spent {} ms, timer {}", allWithFilters.size(), (endTime - startTime), responseTimeInMilliSeconds);
        return allWithFilters;
    }

    private void validateColumnName(List<FilterDto> filters) {
        filters.forEach(x -> SearchColumnVacancy.getByNameIgnoringCase(x.getColumn()));
    }

    public void createVacanciesList(List<Vacancy> vacancies) {
        vacancyRepository.saveAll(vacancies);
        log.info("[ELASTIC] list of vacancies is synchronized successful");
    }

    public List<Vacancy> findAllWithSearch(SearchDto searchDto) {
        SearchCriteriaDto searchCriteriaDto =
                searchCriteriaDtoMapper.convertToSearchCriteriaDto(searchDto);
        return findAllWithFilters(searchCriteriaDto);
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }
}
