package com.hodik.elastic.mappers;

import com.hodik.elastic.dto.VacancyDto;
import com.hodik.elastic.model.Vacancy;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VacancyMapper {
    private final ModelMapper modelMapper;

    @Autowired
    public VacancyMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Vacancy convertToVacancy(VacancyDto vacancyDto) {
        return modelMapper.map(vacancyDto, Vacancy.class);
    }
    public VacancyDto convertToVacancyDto(Vacancy vacancy) {
        return modelMapper.map(vacancy, VacancyDto.class);
    }
}
