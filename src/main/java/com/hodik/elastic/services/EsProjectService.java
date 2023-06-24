package com.hodik.elastic.services;

import com.hodik.elastic.dto.SearchCriteriaDto;
import com.hodik.elastic.dto.SearchFilter;
import com.hodik.elastic.exceptions.EntityAlreadyExitsException;
import com.hodik.elastic.model.Project;
import com.hodik.elastic.repositories.ProjectRepository;
import com.hodik.elastic.repositories.ProjectSearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EsProjectService {

    private final ProjectRepository projectRepository;


   private final ProjectSearchRepository projectSearchRepository;



    @Autowired
    public EsProjectService(ProjectRepository projectRepository, ProjectSearchRepository projectSearchRepository) {
        this.projectRepository = projectRepository;
        this.projectSearchRepository = projectSearchRepository;

    }

    public void createProject(Project project) throws EntityAlreadyExitsException {
        long id = project.getId();
        if (projectRepository.findById(id).isPresent()) {
            throw new EntityAlreadyExitsException("Project already exits id= " + id);
        }
        projectRepository.save(project);
    }

    public void updateProject(Project project) {
        projectRepository.save(project);
    }

    public void deleteProject(long id) {
        projectRepository.deleteById(id);
    }

    public Iterable<Project> findAll() {
        return projectRepository.findAll();
    }

    public Iterable<Project> findAllWithFilters(SearchCriteriaDto searchCriteriaDto) {
        List<SearchFilter> filters = searchCriteriaDto.getFilters();
        if (filters == null) {
            return findAll();
        }
       return projectSearchRepository.findAllWithFilters(searchCriteriaDto);

    }
}