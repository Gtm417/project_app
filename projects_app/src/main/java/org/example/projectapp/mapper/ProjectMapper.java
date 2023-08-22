package org.example.projectapp.mapper;

import org.example.projectapp.mapper.dto.ProjectElasticDto;
import org.example.projectapp.model.Project;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ProjectMapper {
    private final ModelMapper modelMapper;

    public ProjectMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Project convertToProject(ProjectElasticDto projectDto) {
        return modelMapper.map(projectDto, Project.class);
    }

    public ProjectElasticDto convertToProjectElasticDto(Project project) {
        return ProjectElasticDto.builder()
                .id(project.getId())
                .name(project.getName())
                .category(project.getCategory())
                .createdDate(project.getCreateDate())
                .description(project.getDescription())
                .finalPlannedDate(project.getScheduledEndDate())
                .isCommercial(project.isCommercial())
                .isPrivate(project.isPrivate())
                .startDate(project.getStartDate())
                .status(project.getStatus())
                .build();
    }
}
