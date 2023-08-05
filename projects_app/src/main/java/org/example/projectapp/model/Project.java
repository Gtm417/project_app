package org.example.projectapp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", unique = true)
    private String name;
    @Column(name = "private")
    private boolean isPrivate;
    @Column(name = "create_date")
    private LocalDateTime createDate;
    @Column(name = "final_planned_date")
    private LocalDateTime scheduledEndDate;
    @Column(name = "start_date")
    private LocalDateTime startDate;
    @Column(name = "category")
    private String category;
    @Column(name = "description")
    private String description;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "project_status")
    private ProjectStatus status;
    @Column(name = "commercial")
    private boolean isCommercial;
    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    private Set<ProjectMember> projectMembers;
    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    private Set<ProjectNotification> usersToNotify;
    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Vacancy> vacancies;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public LocalDateTime getScheduledEndDate() {
        return scheduledEndDate;
    }

    public void setScheduledEndDate(LocalDateTime scheduledEndDate) {
        this.scheduledEndDate = scheduledEndDate;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public void setStatus(ProjectStatus status) {
        this.status = status;
    }

    public boolean isCommercial() {
        return isCommercial;
    }

    public void setCommercial(boolean commercial) {
        isCommercial = commercial;
    }

    public Set<ProjectMember> getProjectMembers() {
        return projectMembers;
    }

    public void setProjectMembers(Set<ProjectMember> projectMembers) {
        this.projectMembers = projectMembers;
    }

    public Set<ProjectNotification> getUsersToNotify() {
        return usersToNotify;
    }

    public void setUsersToNotify(Set<ProjectNotification> usersToNotify) {
        this.usersToNotify = usersToNotify;
    }

    public Set<Vacancy> getVacancies() {
        return vacancies;
    }

    public void setVacancies(Set<Vacancy> vacancies) {
        this.vacancies = vacancies;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Project)) return false;
        Project project = (Project) o;
        return isPrivate == project.isPrivate &&
                isCommercial == project.isCommercial &&
                Objects.equals(id, project.id) &&
                Objects.equals(name, project.name) &&
                Objects.equals(createDate, project.createDate) &&
                Objects.equals(scheduledEndDate, project.scheduledEndDate) &&
                Objects.equals(startDate, project.startDate) &&
                Objects.equals(category, project.category) &&
                Objects.equals(description, project.description) &&
                status == project.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, isPrivate, createDate, scheduledEndDate, startDate, category,
                description, status, isCommercial, usersToNotify);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("name", name)
                .append("isPrivate", isPrivate)
                .append("createDate", createDate)
                .append("scheduledEndDate", scheduledEndDate)
                .append("startDate", startDate)
                .append("category", category)
                .append("description", description)
                .append("status", status)
                .append("isCommercial", isCommercial)
                .toString();
    }
}
