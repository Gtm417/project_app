package com.hodik.performance.test.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling
public class PerformanceTestApp {

    private static int NUMBER = 1;

    public static void main(String[] args) {

        ConfigurableApplicationContext run = SpringApplication.run(PerformanceTestApp.class, args);
//        CreateRandomProjects randomProjects = new CreateRandomProjects();
//        List<Project> projects = randomProjects.createRandomProjects(NUMBER);
//        ProjectService projectService = run.getBean(ProjectService.class);
//        projectService.saveProjectsList(projects);
//
//        CreateRandomUsers randomUsers = new CreateRandomUsers();
//        List<User> users = randomUsers.createUsers(NUMBER);
//        UserService userService = run.getBean(UserService.class);
//        userService.saveUsersList(users);
//
//        CreateRandomVacancies randomVacancies = new CreateRandomVacancies(projectService);
//        List<Vacancy> vacancies = randomVacancies.createVacancies(NUMBER);
//        VacancyService vacancyService = run.getBean(VacancyService.class);
//        vacancyService.saveVacanciesList(vacancies);

    }
}