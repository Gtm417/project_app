package org.example.projectapp.repository;

import org.example.projectapp.model.ProjectNotification;
import org.example.projectapp.model.ProjectUserCompositeKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectNotificationRepository extends JpaRepository<ProjectNotification, ProjectUserCompositeKey> {
}
