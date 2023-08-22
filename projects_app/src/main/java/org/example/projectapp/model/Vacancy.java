package org.example.projectapp.model;


import lombok.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.util.Objects;
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

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToMany
    @JoinTable(
            name = "vacancy_subscribers",
            joinColumns = @JoinColumn(name = "vacancy_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> subscribers;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vacancy)) return false;
        Vacancy vacancy = (Vacancy) o;
        return Objects.equals(id, vacancy.id) &&
                Objects.equals(creator, vacancy.creator) &&
                Objects.equals(description, vacancy.description) &&
                Objects.equals(aboutProject, vacancy.aboutProject) &&
                Objects.equals(expected, vacancy.expected) &&
                Objects.equals(jobPosition, vacancy.jobPosition) &&
                Objects.equals(project, vacancy.project);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, creator, description, aboutProject, expected, jobPosition, project);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("id", id)
                .append("creator", creator)
                .append("description", description)
                .append("aboutProject", aboutProject)
                .append("expected", expected)
                .append("jobPosition", jobPosition)
                .append("project", project.getId() != null ? project.getId() : null)
                .toString();
    }
}
