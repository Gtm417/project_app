package org.example.projectapp.model;


import lombok.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.util.Set;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "vacancies")
public class Vacancy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "contact")
    private String creator;

    @Column(name = "description")
    private String description;

    @Column(name = "about_project")
    private String aboutProject;

    @Column(name = "expected")
    private String expected;

    @Column(name = "job_position")
    private String jobPosition;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToMany
    @JoinTable(
            name = "vacancy_subscribers",
            joinColumns = @JoinColumn(name = "vacancy_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> subscribers;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("id", id)
                .append("creator", creator)
                .append("description", description)
                .append("aboutProject", aboutProject)
                .append("expected", expected)
                .append("jobPosition", jobPosition)
                .append("project", project)
                .toString();
    }
}
