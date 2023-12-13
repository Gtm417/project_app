package com.hodik.performance.test.app.schedule;

import com.hodik.performance.test.app.config.Config;
import com.hodik.performance.test.app.model.Project;
import com.hodik.performance.test.app.model.User;
import com.hodik.performance.test.app.model.Vacancy;
import com.hodik.performance.test.app.service.ApiTestService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Log4j2
@Service
public class ApiCallScheduler {
    @Autowired
    private Config config;
    @Autowired
    private ApiTestService apiTestService;

    @Scheduled(fixedRateString = "${request.per.second}")
    public void runLoadProjectSearchTest() {
        log.info("scheduled projects started");
        // Trigger parallel requests
        List<CompletableFuture<List<Project>>> futures = new ArrayList<>();
        for (int i = 0; i < config.getRequestAmount(); i++) {
            futures.add(apiTestService.searchProjects());
        }

        // Wait for all requests to complete
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        log.info("scheduled projects finished");
    }

    @Scheduled(fixedRateString = "${request.per.second}")
    public void runLoadUserSearchTest() {
        log.info("scheduled users started");
        // Trigger parallel requests
        List<CompletableFuture<List<User>>> futures = new ArrayList<>();
        for (int i = 0; i < config.getRequestAmount(); i++) {
            futures.add(apiTestService.searchUsers());
        }

        // Wait for all requests to complete
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        log.info("scheduled users finished");

    }

    @Scheduled(fixedRateString = "${request.per.second}")
    public void runLoadVacancySearchTest() {
        log.info("scheduled vacancies started");
        // Trigger parallel requests
        List<CompletableFuture<List<Vacancy>>> futures = new ArrayList<>();
        for (int i = 0; i < config.getRequestAmount(); i++) {
            futures.add(apiTestService.searchVacancies());
        }

        // Wait for all requests to complete
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        log.info("scheduled vacancies finished");

    }

    //
}
