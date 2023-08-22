package org.example.projectapp.mapper;

import org.example.projectapp.mapper.dto.VacancyElasticDto;
import org.example.projectapp.model.Vacancy;
import org.springframework.stereotype.Component;

@Component
public class VacancyMapper {
    public VacancyElasticDto convertToVacancyElasticDto(Vacancy vacancy) {
        return VacancyElasticDto.builder()
                .id(vacancy.getId())
                .jobPosition(vacancy.getJobPosition())
                .projectId(vacancy.getProject().getId())
                .aboutProject(vacancy.getAboutProject())
                .description(vacancy.getDescription())
                .expected(vacancy.getExpected())
                .creator(vacancy.getCreator())
                .build();
    }
}
