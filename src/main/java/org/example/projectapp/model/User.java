package org.example.projectapp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "role")
    private Role role;
    @Column(name = "description")
    private String description;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "user_status")
    private Status status;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "user_type")
    private UserType type;
    @Column(name = "picture")
    private byte[] picture;
    @Column(name = "CV")
    private byte[] cv;
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<ProjectMember> memberOfProjects;
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<ProjectNotification> projectNotifications;
    @ManyToMany(mappedBy = "subscribers", fetch = FetchType.LAZY)
    private Set<Vacancy> subVacancies;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("id", id)
                .append("email", email)
                .append("password", password)
                .append("firstName", firstName)
                .append("lastName", lastName)
                .append("role", role)
                .append("description", description)
                .append("status", status)
                .append("type", type)
                .append("picture", picture)
                .append("cv", cv)
                .append("memberOfProjects", memberOfProjects)
                .toString();
    }
}
