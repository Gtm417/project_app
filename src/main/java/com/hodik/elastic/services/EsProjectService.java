package com.hodik.elastic.services;

import com.hodik.elastic.dto.SearchCriteriaDto;
import com.hodik.elastic.dto.SearchFilter;
import com.hodik.elastic.exceptions.EntityAlreadyExitsException;
import com.hodik.elastic.model.Project;
import com.hodik.elastic.repositories.ProjectRepository;
import com.hodik.elastic.repositories.ProjectSearchRepository;
import com.hodik.elastic.util.SearchColumnProject;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
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
        log.info("Project is saved to ES successful id ="+ id);
    }

    public void updateProject(long id, Project project) {
        project.setId(id);
        projectRepository.save(project);
        log.info("Project is updated in ES successful id ="+ id);
    }

    public void deleteProject(long id) {
        projectRepository.deleteById(id);
        log.info("Project is deleted from ES successful id ="+ id);
    }

    public List<Project> findAll() {
        List<Project> projects= new ArrayList<>();

        projectRepository.findAll().forEach(projects::add);
        return projects;
    }

    public List<Project> findAllWithFilters(SearchCriteriaDto searchCriteriaDto) {
        //validation column name
        searchCriteriaDto.getFilters().stream().forEach(x-> SearchColumnProject.getByNameIgnoringCase(x.getColumn()));
        List<SearchFilter> filters = searchCriteriaDto.getFilters();
        if (filters == null) {
            return findAll();
        }
       return projectSearchRepository.findAllWithFilters(searchCriteriaDto);

    }

    public Optional<Project> findById(long id) {
        return projectRepository.findById(id);
    }
}