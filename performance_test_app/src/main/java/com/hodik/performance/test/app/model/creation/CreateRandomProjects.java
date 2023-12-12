package com.hodik.performance.test.app.model.creation;

import com.hodik.performance.test.app.model.Project;
import com.hodik.performance.test.app.model.ProjectStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class CreateRandomProjects {
    private final Random random = new Random();


    private final List<String> categories = List.of("JavaScript", "HTML/CSS", "SQL",
            "Python", "TypeScript", "Java", "Bash/Shell", "C#", "C++", "PHP", ".NET");

    private final List<String> names = List.of("QA", "name", "backend",
            "frontend", "design");
    private final List<String> descriptions = List.of("Our company works with startups - develop launch and improve them with modern technologies.",
            "Our entire friendly team of talented software engineers work remotely, but despite this, have well-established communication and you will always get help from your teammates.",
            "We offer comfortable working conditions on a remote basis and the opportunity to grow in our team.",
            "If you are interested in our vacancy please start your message with Hello Octy!",
            "Join our project in the technology industry.",
            "Currently, the team consists of 5 developers, and we are currently seeking a Back-End developer.",
            "Success and a diverse global culture make it the perfect choice for your next career move.",
            "They are a double unicorn and growing rapidly, so now is a great time to join!",
            "When you join them, you will embark on a new and exciting career where you will enjoy professional success, gain valuable skills, and make lifelong friends.");


    public List<Project> createRandomProjects(int number) {
        List<Project> projects = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            Project project = new Project();
            String category = categories.get(random.nextInt(categories.size()));
            String name = category + " " + names.get(random.nextInt(names.size()));
            String description = descriptions.get(random.nextInt(descriptions.size()));


            LocalDateTime createdDate = generateRandomDate();
            LocalDateTime startDate = createdDate.plusWeeks(random.nextInt(40));
            LocalDateTime scheduledEndDate = startDate.plusYears(random.nextInt(11));
            ProjectStatus status;
            if (startDate.isBefore(LocalDateTime.now())) {
                status = ProjectStatus.NEW;
            }
            status = ProjectStatus.ON_GOING;

            project = Project.builder()
                    .name(name)
                    .category(category)
                    .createdDate(createdDate)
                    .isPrivate(random.nextBoolean())
                    .isCommercial(random.nextBoolean())
                    .startDate(startDate)
                    .scheduledEndDate(scheduledEndDate)
                    .description(description)
                    .status(status)
                    .build();
            projects.add(project);

        }
        return projects;
    }

    private LocalDateTime generateRandomDate() {

        // Generate a random year between 1900 and 2023 (adjust as needed)
        int year = 2019 + random.nextInt(5);

        // Generate a random month between 1 and 12
        int month = 1 + random.nextInt(12);

        // Generate a random day between 1 and the maximum days in the selected month
        int day = 1 + random.nextInt(LocalDateTime.of(year, month, 1, 0, 0).getMonthValue());

        return LocalDateTime.of(year, month, day, 0, 0);
    }

}
