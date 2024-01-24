package com.hodik.performance.test.app;

import com.hodik.performance.test.app.model.Project;
import com.hodik.performance.test.app.model.User;
import com.hodik.performance.test.app.model.Vacancy;
import com.hodik.performance.test.app.model.creation.CreateRandomProjects;
import com.hodik.performance.test.app.model.creation.CreateRandomUsers;
import com.hodik.performance.test.app.model.creation.CreateRandomVacancies;
import com.hodik.performance.test.app.service.ProjectService;
import com.hodik.performance.test.app.service.UserService;
import com.hodik.performance.test.app.service.VacancyService;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.List;

@SpringBootApplication
@EnableAsync
@EnableScheduling
@Log4j2
public class PerformanceTestApp {

    private static int NUMBER = 10000;

    public static void main(String[] args) {

        ConfigurableApplicationContext run = SpringApplication.run(PerformanceTestApp.class, args);
//        CreateRandomProjects randomProjects = new CreateRandomProjects();
//        List<Project> projects = randomProjects.createRandomProjects(NUMBER);
        ProjectService projectService = run.getBean(ProjectService.class);
//        projectService.saveProjectsList(projects);
//        log.info("Projects saved");
//        CreateRandomUsers randomUsers = new CreateRandomUsers();
//        List<User> users = randomUsers.createUsers(NUMBER);
//        UserService userService = run.getBean(UserService.class);
//        userService.saveUsersList(users);
////        log.info("users saved");
        CreateRandomVacancies randomVacancies = new CreateRandomVacancies(projectService);
        List<Vacancy> vacancies = randomVacancies.createVacancies(NUMBER);
        VacancyService vacancyService = run.getBean(VacancyService.class);
        vacancyService.saveVacanciesList(vacancies);
        log.info("vacancies saved");

    }
}