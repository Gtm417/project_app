package org.example.projectapp.service.impl;

import org.example.projectapp.auth.AuthService;
import org.example.projectapp.controller.VacancyController;
import org.example.projectapp.controller.dto.SearchDto;
import org.example.projectapp.controller.dto.VacancyDto;
import org.example.projectapp.mapper.SearchElasticCriteriaDtoMapper;
import org.example.projectapp.mapper.VacancyMapper;
import org.example.projectapp.mapper.dto.SearchElasticCriteriaDto;
import org.example.projectapp.mapper.dto.VacancyElasticDto;
import org.example.projectapp.model.Project;
import org.example.projectapp.model.User;
import org.example.projectapp.model.Vacancy;
import org.example.projectapp.repository.ProjectRepository;
import org.example.projectapp.repository.VacancyRepository;
import org.example.projectapp.restclient.ElasticVacanciesServiceClient;
import org.example.projectapp.service.VacancyService;
import org.example.projectapp.service.exception.CustomEntityNotFoundException;
import org.example.projectapp.service.exception.ProjectNotMatchException;
import org.example.projectapp.service.notification.EventNotificationService;
import org.example.projectapp.service.notification.message.VacancySubscriptionMessageDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@Service
public class VacancyServiceImpl implements VacancyService {
    private final ProjectRepository projectRepository;
    private final VacancyRepository vacancyRepository;
    private final AuthService authService;
    private final EventNotificationService notificationService;
    private final ElasticVacanciesServiceClient elasticVacanciesServiceClient;
    private final VacancyMapper vacancyMapper;
    private final SearchElasticCriteriaDtoMapper elasticCriteriaDtoMapper;
    private final Logger logger = LoggerFactory.getLogger(VacancyController.class);

    public VacancyServiceImpl(ProjectRepository projectRepository, VacancyRepository vacancyRepository,
                              AuthService authService, EventNotificationService notificationService,
                              ElasticVacanciesServiceClient elasticVacanciesServiceClient,
                              VacancyMapper vacancyMapper,
                              @Qualifier("searchVacancyElasticCriteriaDtoMapper") SearchElasticCriteriaDtoMapper elasticCriteriaDtoMapper) {
        this.projectRepository = projectRepository;
        this.vacancyRepository = vacancyRepository;
        this.authService = authService;
        this.notificationService = notificationService;
        this.elasticVacanciesServiceClient = elasticVacanciesServiceClient;
        this.vacancyMapper = vacancyMapper;
        this.elasticCriteriaDtoMapper = elasticCriteriaDtoMapper;
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#dto.projectId,'org.example.projectapp.model.Project', 'vacancies:write')")
    public VacancyDto createVacancy(VacancyDto dto) {
        Vacancy vacancy = getVacancy(dto);

        Vacancy save = vacancyRepository.save(vacancy);
        VacancyElasticDto vacancyElasticDto = vacancyMapper.convertToVacancyElasticDto(vacancy);
        elasticVacanciesServiceClient.createVacancy(vacancyElasticDto);
        return dto;
    }

    private Vacancy getVacancy(VacancyDto dto) {
        Project project = projectRepository.findById(dto.getProjectId())
                .orElseThrow(() -> new CustomEntityNotFoundException(dto.getProjectId(), Project.class.getName()));

        return Vacancy.builder()
                .creator(authService.getUserEmail())
                .aboutProject(dto.getAboutProject())
                .description(dto.getDescription())
                .expected(dto.getExpected())
                .jobPosition(dto.getJobPosition())
                .project(project)
                .build();
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

        boolean anyMatch = vacancy.getSubscribers().contains(userFromAuth);
        if (anyMatch) {
            return;
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

        if (vacancy.getSubscribers() == null ||
                !vacancy.getSubscribers().contains(userFromAuth)) {
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

    @Override
    public List<Vacancy> findVacanciesByListId(List<Long> ids) {
        return vacancyRepository.findAllById(ids);
    }

    @Override
    public List<Vacancy> findAllVacancies() {
        return vacancyRepository.findAll();
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#dto.projectId,'org.example.projectapp.model.Project', 'vacancies:write')")
    public VacancyDto updateVacancy(Long vacancyId, VacancyDto dto) {
        tryGetVacancy(vacancyId);
        Vacancy vacancy = getVacancy(dto);
        vacancy.setId(vacancyId);
        vacancyRepository.save(vacancy);
        VacancyElasticDto vacancyElasticDto = vacancyMapper.convertToVacancyElasticDto(vacancy);
        elasticVacanciesServiceClient.updateVacancy(vacancyId, vacancyElasticDto);
        return VacancyDto.builder()
                .projectId(vacancy.getProject().getId())
                .jobPosition(vacancy.getJobPosition())
                .aboutProject(vacancy.getAboutProject())
                .expected(vacancy.getExpected())
                .description(vacancy.getDescription())
                .build();
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#projectId,'org.example.projectapp.model.Project', 'vacancies:write')")
    public void deleteVacancy(Long projectId, Long vacancyId) {
        Vacancy vacancy = tryGetVacancy(vacancyId);
        if (!Objects.equals(projectId, vacancy.getProject().getId())) {
            logger.info("[PROJECT] Vacancy couldn't delete. Project with id={} doesn't have vacancy id= {}", projectId, vacancyId);
            throw new ProjectNotMatchException(String.format("Project with id=%s doesn't have vacancy id=%s", projectId, vacancyId));
        }
        vacancyRepository.deleteById(vacancyId);
        elasticVacanciesServiceClient.deleteVacancy(vacancyId);
    }

    @Override
    public List<VacancyDto> findVacanciesInElastic(SearchDto searchDto) {
        SearchElasticCriteriaDto searchElasticCriteriaDto =
                elasticCriteriaDtoMapper.convertToSearchElasticCriteriaDto(searchDto);
        return elasticVacanciesServiceClient.searchVacancies(searchElasticCriteriaDto);
    }

    private Vacancy tryGetVacancy(Long vacancyId) {
        return vacancyRepository.findById(vacancyId)
                .orElseThrow(() -> new CustomEntityNotFoundException(vacancyId, Vacancy.class.getSimpleName()));

    }
}
