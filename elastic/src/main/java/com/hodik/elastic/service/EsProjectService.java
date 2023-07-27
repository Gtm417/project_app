package com.hodik.elastic.service;

import com.hodik.elastic.dto.SearchCriteriaDto;
import com.hodik.elastic.dto.SearchFilter;
import com.hodik.elastic.exception.EntityAlreadyExistsException;
import com.hodik.elastic.mapper.PageableMapper;
import com.hodik.elastic.model.Project;
import com.hodik.elastic.repository.ProjectRepository;
import com.hodik.elastic.repository.ProjectSearchRepository;
import com.hodik.elastic.util.SearchColumnProject;
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
public class EsProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectSearchRepository projectSearchRepository;
    private final PageableMapper pageableMapper;


    @Autowired
    public EsProjectService(ProjectRepository projectRepository,  ProjectSearchRepository projectSearchRepository, PageableMapper pageableMapper) {
        this.projectRepository = projectRepository;

        this.projectSearchRepository = projectSearchRepository;

        this.pageableMapper = pageableMapper;
    }

    public void createProject(Project project) throws EntityAlreadyExistsException {
        long id = project.getId();
        if (projectRepository.findById(id).isPresent()) {

            throw new EntityAlreadyExistsException("Project already exits id= " + id);
        }
        projectRepository.save(project);
        log.info("Project is saved to ES successful id =" + id);
    }

    public void updateProject(long id, Project project) {
        project.setId(id);
        projectRepository.save(project);
        log.info("Project is updated in ES successful id =" + id);
    }

    public void deleteProject(long id) {
        projectRepository.deleteById(id);
        log.info("Project is deleted from ES successful id =" + id);
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
        List<SearchFilter> filters = searchCriteriaDto.getFilters();
        if (CollectionUtils.isEmpty(filters)) {
            return findAll(pageableMapper.getPageable(searchCriteriaDto));
        }
        //validation column name
        filters.forEach(x -> SearchColumnProject.getByNameIgnoringCase(x.getColumn()));
        return projectSearchRepository.findAllWithFilters(searchCriteriaDto);

    }

    public Optional<Project> findById(long id) {
        return projectRepository.findById(id);
    }
}