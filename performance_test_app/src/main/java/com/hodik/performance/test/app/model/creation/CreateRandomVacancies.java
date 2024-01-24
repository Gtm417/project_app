package com.hodik.performance.test.app.model.creation;

import com.hodik.performance.test.app.model.Project;
import com.hodik.performance.test.app.model.Vacancy;
import com.hodik.performance.test.app.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Component

public class CreateRandomVacancies {
    private final Random random = new Random();
    @Autowired
    private ProjectService projectService;

    public CreateRandomVacancies(ProjectService projectService) {
        this.projectService = projectService;
    }

    private List<String> firstNames = List.of("Liam", "Olivia", "Noah",
            "Emma", "Oliver", "Charlotte", "James", "Amelia", "Elijah", "Sophia",
            "William", "Isabella",
            "Henry", "Ava",
            "Lucas", "Mia", "Benjamin", "Evelyn",
            "Theodore", "Luna");
    private List<String> lastNames = List.of("Elsher",
            "Solace", "Levine", "Thatcher", "Raven", "Bardot", "St.James",
            "Hansley", "Cromwell", "Ashley", "Monroe", "West", "Langley", "Daughtler",
            "Madison", "Marley", "Ellis", "Hope", "Cassidy", "Lopez", "Jenkins", "Poverly", "McKenna", "Gonzales", "Keller");
    private String[] emailSuffix = {"@gmail.com", "@ukr.net.com", "@i.com"};

    private List<String> descriptions = List.of("1. Bachelor's degree in Computer Science, Information Technology, or a related field.\n" +
                    "2. 3+ years of experience in software development, with a focus on data integration and API development.\n" +
                    "3. Strong proficiency in programming languages such as Python, JavaScript, or Java.\n" +
                    "4. Experience working with RESTful APIs and web services.",
            "- Proficiency in Spring, especially Spring Boot.\n" +
                    "- Skill in working with SQL and NoSQL databases.\n" +
                    "- Ability to design and maintain APIs.\n" +
                    "- English B2 or upper.",
            "5+ years of commercial development experience using Java-related technologies\n" +
                    "2+ years of working experience with API development\n" +
                    "Hands-on experience with microservice architecture\n" +
                    "Ambition to write high-quality code covered with unit tests\n" +
                    "Solid debugging and troubleshooting skills",
            "Proactive and result-oriented mindset\n" +
                    "Strong problem-solving skills\n" +
                    "At least Upper-Intermediate English level",
            "As a Backend Developer you will be part of our ad server team. You will optimize a high-scale infrastructure product with billions of transactions every day, own end to end features and handle the entire development cycle - architecture, implementation, deployment and monitoring.",
            "As part of the job, you will work closely with the product, BI, operations and business teams. build a great product that will leverage our capabilities and directly influence the way billions of people consume and engage with content over the internet.",
            "Be dedicated to product quality and have excellent analytical and problem-solving skills\n" +
                    "Continuously design, develop, and deploy backend services\n" +
                    "Focus on high availability, low latency, and scalability",
            "The person will be responsible for the solution design and support design implementation with development teams of a high-quality solution to enable integrations and data management within Intelligent Deployment project.");
    private List<String> aboutProjectList = List.of("Our company works with startups - develop launch and improve them with modern technologies.",
            "Our entire friendly team of talented software engineers work remotely, but despite this, have well-established communication and you will always get help from your teammates.",
            "We offer comfortable working conditions on a remote basis and the opportunity to grow in our team.",
            "If you are interested in our vacancy please start your message with Hello Octy!",
            "Join our project in the technology industry.",
            "Currently, the team consists of 5 developers, and we are currently seeking a Back-End developer.",
            "Success and a diverse global culture make it the perfect choice for your next career move.",
            "They are a double unicorn and growing rapidly, so now is a great time to join!",
            "When you join them, you will embark on a new and exciting career where you will enjoy professional success, gain valuable skills, and make lifelong friends.");
    private List<String> expectedList = List.of("Python", "HTML5", "JavaScript", "CSS", "PHP", "SQL", "C++", "Ruby", ".NET",
            "Linux", "Windows", "masOS", "Android", "iOS",
            "Azure", "AWS", "Google Cloud", "Amazon Web", "Kamatera", "Oracle",
            "Shopify", "WooCommerce", " BigCommerce", "Magento", "OpenCart");
    private List<String> jobPositions = List.of("Chief Architect", "Software Architect",
            "Engineering Project", "Manager/Engineering", "Manager",
            "Technical Lead/Engineering", "Lead/Team Lead",
            "Principal Software Engineer",
            "Senior Software Engineer", "Senior Software Developer",
            "Software Engineer",
            "Software Developer",
            "Junior Software Developer",
            "Intern Software Developer");


    public List<Vacancy> createVacancies(int number) {
        List<Vacancy> vacancies = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            Vacancy vacancy;


            String firstName = firstNames.get(random.nextInt(firstNames.size()));
            String lastName = lastNames.get(random.nextInt(lastNames.size()));
            String creator = firstName + "_" + lastName + i + emailSuffix[random.nextInt(emailSuffix.length)];

            String description = descriptions.get(random.nextInt(descriptions.size()));

            String aboutProject = aboutProjectList.get(random.nextInt(aboutProjectList.size()));

            String expected = getExpected() + " " + getExpected() + " " + getExpected() + " " + getExpected();

            String jobPosition = jobPositions.get(random.nextInt(jobPositions.size()));
            Project project = projectService.findRandomProject();

            vacancy = Vacancy.builder()
                    .aboutProject(trimToSize(aboutProject))
                    .creator(creator)
                    .description(trimToSize(description))
                    .expected(trimToSize(expected))
                    .jobPosition(jobPosition)
                    .project(project)
                    .build();
            vacancies.add(vacancy);
        }
        return vacancies;
    }

    private String getExpected() {
        return expectedList.get(random.nextInt(expectedList.size()));
    }

    public String getSearch() {
        List<String> allLists = new ArrayList<>();
        allLists.addAll(firstNames);
        allLists.addAll(lastNames);
        allLists.addAll(lastNames);
        allLists.addAll(toListOfWords(descriptions));
        allLists.addAll(toListOfWords(aboutProjectList));
        allLists.addAll(expectedList);
        allLists.addAll(jobPositions);
        return allLists.get(random.nextInt(allLists.size()));
    }

    public List<String> toListOfWords(List<String> sentences) {
        List<String> words = new ArrayList<>();
        for (String sentence : sentences) {
            String[] sentenceWords = sentence.split("\\s+");
            words.addAll(Arrays.asList(sentenceWords));
        }
        return words;
    }

    private String trimToSize(String s) {
       return s.substring(0, Math.min(s.length(), 250));
    }
}
