package org.example.projectapp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "interested_in_project")
public class ProjectNotification {

    @EmbeddedId
    private ProjectUserCompositeKey id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne
    @MapsId("projectId")
    @JoinColumn(name = "project_id")
    private Project project;

    @Column(name = "notification_enabled")
    private Boolean notificationEnabled;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProjectNotification)) return false;
        ProjectNotification that = (ProjectNotification) o;
        return Objects.equals(id, that.id) && Objects.equals(user, that.user) &&
                Objects.equals(project, that.project) &&
                Objects.equals(notificationEnabled, that.notificationEnabled);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, project, notificationEnabled);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("id", id)
                .append("user", user)
                .append("notificationEnabled", notificationEnabled)
                .toString();
    }
}
