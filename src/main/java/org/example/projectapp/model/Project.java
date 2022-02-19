package org.example.projectapp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.time.LocalDateTime;
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
    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
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
                .append("projectMembers", projectMembers)
                .toString();
    }
}
