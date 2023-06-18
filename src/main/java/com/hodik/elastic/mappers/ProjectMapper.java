package com.hodik.elastic.mappers;

import com.hodik.elastic.dto.ProjectDto;
import com.hodik.elastic.model.Project;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProjectMapper {
    private final ModelMapper modelMapper;

    @Autowired
    public ProjectMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Project convertToProject(ProjectDto projectDto) {
        return modelMapper.map(projectDto, Project.class);
    }
}
