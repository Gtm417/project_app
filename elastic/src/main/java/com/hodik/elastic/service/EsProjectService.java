package com.hodik.elastic.service;

import com.hodik.elastic.dto.FilterDto;
import com.hodik.elastic.dto.SearchCriteriaDto;
import com.hodik.elastic.dto.SearchDto;
import com.hodik.elastic.exception.EntityAlreadyExistsException;
import com.hodik.elastic.mapper.PageableMapper;
import com.hodik.elastic.mapper.SearchCriteriaDtoMapper;
import com.hodik.elastic.model.Project;
import com.hodik.elastic.repository.ProjectRepository;
import com.hodik.elastic.repository.ProjectSearchRepository;
import com.hodik.elastic.util.SearchColumnProject;
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
public class EsProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectSearchRepository projectSearchRepository;
    private final PageableMapper pageableMapper;
    private final SearchCriteriaDtoMapper searchCriteriaDtoMapper;


    @Autowired
    public EsProjectService(ProjectRepository projectRepository,
                            ProjectSearchRepository projectSearchRepository,
                            PageableMapper pageableMapper,
                            @Qualifier("searchProjectCriteriaDtoMapper") SearchCriteriaDtoMapper searchCriteriaDtoMapper) {
        this.projectRepository = projectRepository;

        this.projectSearchRepository = projectSearchRepository;

        this.pageableMapper = pageableMapper;
        this.searchCriteriaDtoMapper = searchCriteriaDtoMapper;
    }

    public void createProject(Project project) throws EntityAlreadyExistsException {
        long id = project.getId();
        if (projectRepository.findById(id).isPresent()) {
            log.error("[ELASTIC] Project isn't saved. Project already exists id= {}", id);
            throw new EntityAlreadyExistsException("[ELASTIC] Project already exits id= " + id);
        }
        projectRepository.save(project);

        log.info("[ELASTIC] Project is saved to ES successful id =" + id);
    }

    public void updateProject(long id, Project project) {
        project.setId(id);
        projectRepository.save(project);
        log.info("[ELASTIC] Project is updated in ES successful id =" + id);
    }

    public void deleteProject(long id) {
        projectRepository.deleteById(id);
        log.info("[ELASTIC] Project is deleted from ES successful id =" + id);
    }

    public List<Project> findAll() {
        List<Project> projects = new ArrayList<>();
        projectRepository.findAll().forEach(projects::add);
        return projects;
    }

    public List<Project> findAll(Pageable pageable) {
        List<Project> projects = new ArrayList<>();
        projectRepository.findAll(pageable).forEach(projects::add);
        return projects;
    }

    public List<Project> findAllWithFilters(SearchCriteriaDto searchCriteriaDto) {
        List<FilterDto> filters = searchCriteriaDto.getFilters();
        if (CollectionUtils.isEmpty(filters)) {
            return findAll(pageableMapper.getPageable(searchCriteriaDto));
        }
        validateColumnName(filters);
        return projectSearchRepository.findAllWithFilters(searchCriteriaDto);

    }

    private void validateColumnName(List<FilterDto> filters) {
        filters.forEach(x -> SearchColumnProject.getByNameIgnoringCase(x.getColumn()));
    }

    public Optional<Project> findById(long id) {
        return projectRepository.findById(id);
    }

    public void createProjectList(List<Project> projects) {
        projectRepository.saveAll(projects);
        log.info("[ELASTIC] List of projects is saved successful");
    }

    public List<Project> findAllWithSearch(SearchDto searchDto) {
        SearchCriteriaDto searchCriteriaDto =
                searchCriteriaDtoMapper.convertToSearchCriteriaDto(searchDto);
        return findAllWithFilters(searchCriteriaDto);
    }
}