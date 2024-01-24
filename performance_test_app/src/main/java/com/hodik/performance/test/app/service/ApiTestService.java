package com.hodik.performance.test.app.service;

import com.hodik.performance.test.app.config.Config;
import com.hodik.performance.test.app.dto.SearchDto;
import com.hodik.performance.test.app.dto.creation.ProjectSearchCreation;
import com.hodik.performance.test.app.dto.creation.UserSearchCreation;
import com.hodik.performance.test.app.dto.creation.VacanciesSearchCreation;
import com.hodik.performance.test.app.model.Project;
import com.hodik.performance.test.app.model.User;
import com.hodik.performance.test.app.model.Vacancy;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.CompletableFuture;


@Log4j2
@Service
public class ApiTestService {
    private static final String TOKEN = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtYWluX2FwcCIsInJvbGUiOiJST0xFX0FQUCIsImlhdCI6MTcwMjM3NTI2NSwiZXhwIjoyMDE3OTk0NDY1fQ.0kZ538LL82eZ37iLjjaszieWoTvnXEnf7pxitjlwGOk";
    private final Config config;

    private final RestTemplate restTemplate;
    private final ProjectSearchCreation projectSearchCreation;
    private final UserSearchCreation userSearchCreation;
    private final VacanciesSearchCreation vacanciesSearchCreation;

    public ApiTestService(Config config, RestTemplate restTemplate, ProjectSearchCreation projectSearchCreation, UserSearchCreation userSearchCreation, VacanciesSearchCreation vacanciesSearchCreation) {
        this.config = config;
        this.restTemplate = restTemplate;
        this.projectSearchCreation = projectSearchCreation;
        this.userSearchCreation = userSearchCreation;
        this.vacanciesSearchCreation = vacanciesSearchCreation;
    }

//    @Async
    public CompletableFuture<List<User>> searchUsers() {
        String url = config.getUsersUrl();
        log.info(Thread.currentThread() + " Looking up " + url);

        SearchDto searchDto = userSearchCreation.createProjectSearchDto();
        List<User> results = restTemplate.postForObject(url, searchDto, List.class);

        return CompletableFuture.completedFuture(results);
    }

//    @Async
    public CompletableFuture<List<Project>> searchProjects() {
        String url = config.getProjectsUrl();
        Object requestBody = new Object();
        SearchDto searchDto = projectSearchCreation.createProjectSearchDto();

        List<Project> results = restTemplate.postForObject(url, searchDto, List.class);
        log.info(Thread.currentThread() + " Looking up " + url);
        return CompletableFuture.completedFuture(results);
    }

//    @Async
    public CompletableFuture<List<Vacancy>> searchVacancies() {
        String url = config.getVacanciesUrl();
        SearchDto searchDto = vacanciesSearchCreation.createProjectSearchDto();
        List<Vacancy> results = restTemplate.postForObject(url, searchDto, List.class);
        log.info(Thread.currentThread() + " Looking up " + url);


        return CompletableFuture.completedFuture(results);
    }


//    public CompletableFuture<String> callApi(String apiUrl) {
//        CompletableFuture<String> future = new CompletableFuture<>();
//
//        // Make API call asynchronously
//        RequestEntity requestEntity = new RequestEntity();
//        ResponseEntity<Object> exchange = restTemplate.exchange();
//        restTemplate.execute(new URI(), HttpMethod.POST, (req) -> req, response -> response);
//                .addCallback(
//                        response -> {
//                            // On success, complete the future with the result
//                            future.complete(response.getBody());
//                        },
//                        ex -> {
//                            // On error, complete the future exceptionally with the exception
//                            future.completeExceptionally(ex);
//                        });
//
//        return future;
//    }
}
