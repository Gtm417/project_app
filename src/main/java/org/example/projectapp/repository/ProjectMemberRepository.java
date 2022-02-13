package org.example.projectapp.repository;

import org.example.projectapp.model.Project;
import org.example.projectapp.model.ProjectMember;
import org.example.projectapp.model.ProjectRole;
import org.example.projectapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {
    @Query("SELECT pm.projectRole FROM ProjectMember pm WHERE pm.project = :project and pm.user = :user")
    List<ProjectRole> getAllProjectRolesForUser(@Param("project") Project project, @Param("user") User user);

    @Query("SELECT pm.projectRole FROM ProjectMember pm WHERE pm.project.id = :projectId and pm.user = :user")
    List<ProjectRole> getAllProjectRolesForUser(@Param("projectId") Long projectId, @Param("user") User user);
}
