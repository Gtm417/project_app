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
import org.example.projectapp.service.notification.EventNotificationService;
import org.example.projectapp.service.notification.message.VacancySubscriptionMessageDto;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

@Service
public class VacancyServiceImpl implements VacancyService {
    private final ProjectRepository projectRepository;
    private final VacancyRepository vacancyRepository;
    private final AuthService authService;
    private final EventNotificationService notificationService;

    public VacancyServiceImpl(ProjectRepository projectRepository, VacancyRepository vacancyRepository,
                              AuthService authService, EventNotificationService notificationService) {
        this.projectRepository = projectRepository;
        this.vacancyRepository = vacancyRepository;
        this.authService = authService;
        this.notificationService = notificationService;
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
    //todo make email  notification for vacancy  creator
    public void subscribeTo(Long vacancyId) {
        Vacancy vacancy = tryGetVacancy(vacancyId);
        User userFromAuth = authService.getUserFromAuth();

        if (vacancy.getSubscribers() == null) {
            vacancy.setSubscribers(new HashSet<>());
        }

        vacancy.getSubscribers().add(userFromAuth);

        vacancyRepository.save(vacancy);

        List<String> notificationReceivers = List.of(vacancy.getCreator());
        VacancySubscriptionMessageDto notificationMessage = new VacancySubscriptionMessageDto();
        notificationMessage.setSubscribe(true);
        notificationMessage.setUserEmail(userFromAuth.getEmail());
        notificationMessage.setVacancyId(vacancyId);
        notificationMessage.setName("vacancySub");
        notificationMessage.setReceiversEmail(notificationReceivers);
        notificationMessage.setCreatedDate(LocalDateTime.now());
        notificationService.sendNotification(notificationMessage, "vacancy.unsub");
    }

    @Override
    @Transactional
    //todo make email  notification for vacancy  creator
    public void unsubscribeFrom(Long vacancyId) {
        Vacancy vacancy = tryGetVacancy(vacancyId);
        User userFromAuth = authService.getUserFromAuth();

        if (vacancy.getSubscribers() == null) {
            return;
        }
        vacancy.getSubscribers().remove(userFromAuth);

        vacancyRepository.save(vacancy);

        List<String> notificationReceivers = List.of(vacancy.getCreator());
        VacancySubscriptionMessageDto notificationMessage = new VacancySubscriptionMessageDto();
        notificationMessage.setSubscribe(false);
        notificationMessage.setUserEmail(userFromAuth.getEmail());
        notificationMessage.setVacancyId(vacancyId);
        notificationMessage.setName("vacancyUnSub");
        notificationMessage.setReceiversEmail(notificationReceivers);
        notificationService.sendNotification(notificationMessage, "vacancy.unsub");
    }

    private Vacancy tryGetVacancy(Long vacancyId) {
        return vacancyRepository.findById(vacancyId)
                .orElseThrow(() -> new CustomEntityNotFoundException(vacancyId, Vacancy.class.getSimpleName()));

    }
}
