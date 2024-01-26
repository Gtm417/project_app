package com.hodik.performance.test.app.model.creation;

import com.hodik.performance.test.app.model.Role;
import com.hodik.performance.test.app.model.Status;
import com.hodik.performance.test.app.model.User;
import com.hodik.performance.test.app.model.UserType;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class CreateRandomUsers {
    private final Random random = new Random();
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

    private final List<String> descriptions = List.of("Experienced full-stack, full-time, web developer. Developing career started at 2015, all of this time was spent on developing amazing big and small web applications.",
            "Regarding technologies, I’m best in Laravel (php framwork) and Vue.js (Node.js).", "I have experience on writing complicated sql queries and designing database.",
            "My main focus is to build up the fast growing investment company Polynom. When I have time to spare I write stock advice for the subscribers to Goda Tiders Investment Advice and hold intensive courses in how to achieve Financial Freedom.",
            "I am registered as private entrepreneur in Ukraine. It allows me to legally work with direct contracts around the world. However since 2019 I am preferred to work through Upwork platform.",
            "I'm a freelance writer, film producer and talent researcher.",
            "Experienced professional proficient in both web development and graphics design, with a demonstrated history of creating visually appealing and functional digital solutions",
            "Adept at utilizing a variety of programming languages, designing tools, and techniques to deliver high- quality project that exceed client expectations.",
            "In my work I always analyze competitors in order to identify the best solutions and use them in my design.",
            "I am a team player, it is very important for me when people work together, but also I can make solo work and help others.",
            "I use the following programs: Figma, Adobe Illustrator, Adobe Photoshop."
    );

    public List<User> createUsers(int number) {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            User user;
            String firstName = getFirstName();
            String lastName = getLastName();
            String email = firstName + "_" + lastName + i + emailSuffix[random.nextInt(emailSuffix.length)];
            String password = firstName + lastName;
            String description = getDescription();
            Status[] statuses = Status.values();
            Status status = statuses[random.nextInt(statuses.length)];
            UserType[] types = UserType.values();
            UserType type = types[random.nextInt(types.length)];
            user = User.builder()
                    .firstName(firstName)
                    .lastName(lastName)
                    .role(Role.ROLE_USER)
                    .type(type)
                    .email(email)
                    .status(status)
                    .description(description)
                    .password(password)
                    .build();

            users.add(user);
        }
        return users;
    }

    public String getDescription() {
        return descriptions.get(random.nextInt(descriptions.size()));
    }

    public String getLastName() {
        return lastNames.get(random.nextInt(lastNames.size()));
    }

    public String getFirstName() {
        return firstNames.get(random.nextInt(firstNames.size()));
    }

}
