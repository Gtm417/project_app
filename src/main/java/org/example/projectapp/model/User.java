package org.example.projectapp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
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
    Set<ProjectMember> memberOfProjects;
}
