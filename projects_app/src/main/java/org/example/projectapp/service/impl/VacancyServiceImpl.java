package org.example.projectapp.service.impl;

import org.example.projectapp.auth.AuthService;
import org.example.projectapp.controller.dto.VacancyDto;
import org.example.projectapp.model.Project;
import org.example.projectapp.model.User;
import org.example.projectapp.model.Vacancy;
import org.example.projectapp.repository.ProjectRepository;
import org.example.projectapp.repository.VacancyRepository;
import org.example.projectapp.service.VacancyService;
import org.example.projectapp.service.exception.CustomEntityNotFoundException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;

@Service
public class VacancyServiceImpl implements VacancyService {
    private final ProjectRepository projectRepository;
    private final VacancyRepository vacancyRepository;
    private final AuthService authService;

    public VacancyServiceImpl(ProjectRepository projectRepository, VacancyRepository vacancyRepository, AuthService authService) {
        this.projectRepository = projectRepository;
        this.vacancyRepository = vacancyRepository;
        this.authService = authService;
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#dto.projectId,'org.example.projectapp.model.Project', 'vacancies:write')")
    public Vacancy createVacancy(VacancyDto dto) {
        Project project = projectRepository.findById(dto.getProjectId())
                .orElseThrow(() -> new CustomEntityNotFoundException(dto.getProjectId(), Project.class.getName()));

        Vacancy vacancy = Vacancy.builder()
                .creator(authService.getUserEmail())
                .aboutProject(dto.getAboutProject())
                .description(dto.getDescription())
                .expected(dto.getExpected())
                .jobPosition(dto.getJobPosition())
                .project(project)
                .build();

        return vacancyRepository.save(vacancy);
    }

    @Override
    @Transactional
    //todo make email  notification for vacancy  creater
    public void subscribeTo(Long vacancyId) {
        Vacancy vacancy = tryGetVacancy(vacancyId);
        User userFromAuth = authService.getUserFromAuth();

        if (vacancy.getSubscribers() == null) {
            vacancy.setSubscribers(new HashSet<>());
        }

        vacancy.getSubscribers().add(userFromAuth);

        vacancyRepository.save(vacancy);
    }

    @Override
    @Transactional
    //todo make email  notification for vacancy  creater
    public void unsubscribeFrom(Long vacancyId) {
        Vacancy vacancy = tryGetVacancy(vacancyId);
        User userFromAuth = authService.getUserFromAuth();

        if (vacancy.getSubscribers() == null) {
            return;
        }
        vacancy.getSubscribers().remove(userFromAuth);

        vacancyRepository.save(vacancy);
    }

    private Vacancy tryGetVacancy(Long vacancyId) {
        return vacancyRepository.findById(vacancyId)
                .orElseThrow(() -> new CustomEntityNotFoundException(vacancyId, Vacancy.class.getSimpleName()));

    }
}