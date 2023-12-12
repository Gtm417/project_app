package com.hodik.performance.test.app;

import com.hodik.performance.test.app.model.Project;
import com.hodik.performance.test.app.model.User;
import com.hodik.performance.test.app.model.Vacancy;
import com.hodik.performance.test.app.service.ProjectService;
import com.hodik.performance.test.app.service.UserService;
import com.hodik.performance.test.app.service.VacancyService;
import com.hodik.performance.test.app.service_create_model.CreateRandomProjects;
import com.hodik.performance.test.app.service_create_model.CreateRandomUsers;
import com.hodik.performance.test.app.service_create_model.CreateRandomVacancies;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;

@SpringBootApplication
public class PerformanceTestApp {

    private static int NUMBER = 1;

    public static void main(String[] args) {

        ConfigurableApplicationContext run = SpringApplication.run(PerformanceTestApp.class, args);
        CreateRandomProjects randomProjects = new CreateRandomProjects();
        List<Project> projects = randomProjects.createRandomProjects(NUMBER);
        ProjectService projectService = run.getBean(ProjectService.class);
        projectService.saveProjectsList(projects);

        CreateRandomUsers randomUsers = new CreateRandomUsers();
        List<User> users = randomUsers.createUsers(NUMBER);
        UserService userService = run.getBean(UserService.class);
        userService.saveUsersList(users);
        System.out.println(users);

        CreateRandomVacancies randomVacancies = new CreateRandomVacancies(projectService);
        List<Vacancy> vacancies = randomVacancies.createVacancies(NUMBER);
        VacancyService vacancyService = run.getBean(VacancyService.class);
        vacancyService.saveVacanciesList(vacancies);
        System.out.println(vacancies);

    }
}