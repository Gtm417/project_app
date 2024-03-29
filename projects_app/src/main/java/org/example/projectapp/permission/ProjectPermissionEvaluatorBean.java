package org.example.projectapp.permission;

import org.example.projectapp.auth.AuthService;
import org.example.projectapp.model.Project;
import org.example.projectapp.model.ProjectPermission;
import org.example.projectapp.model.ProjectRole;
import org.example.projectapp.model.User;
import org.example.projectapp.service.ProjectMemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

import static org.example.projectapp.model.ProjectRole.OWNER;

public class ProjectPermissionEvaluatorBean implements PermissionEvaluator {
    private final AuthService authService;
    private final ProjectMemberService projectMemberService;
    Logger logger = LoggerFactory.getLogger(ProjectPermissionEvaluatorBean.class);

    public ProjectPermissionEvaluatorBean(AuthService authService, ProjectMemberService projectMemberService) {
        this.authService = authService;
        this.projectMemberService = projectMemberService;
    }

    @Transactional
    @Override
    public boolean hasPermission(Authentication auth, Object targetDomainObject, Object permissionName) {
        if (auth == null || targetDomainObject == null || !(permissionName instanceof String)) {
            logger.info("[PERMISSION] Permission denied: auth={}, target={}, permissionName={}",
                    auth, targetDomainObject, permissionName);
            return false;
        }
        User userFromAuth = authService.getUserFromAuth(auth);
        Project project = (Project) targetDomainObject;
        ProjectRole projectRole = projectMemberService.getAllProjectRolesForUser(project, userFromAuth);
        return hasPermission(projectRole, (String) permissionName);
    }

    @Transactional
    @Override
    public boolean hasPermission(Authentication auth, Serializable targetId, String targetType, Object permissionName) {
        if (auth == null || targetType == null || targetId == null || !(permissionName instanceof String)) {
            logger.info("[PERMISSION] Permission denied: auth={}, targetId={}, targetType={}, permissionName={}",
                    auth, targetId, targetType, permissionName);
            return false;
        }
        User userFromAuth = authService.getUserFromAuth(auth);
        Long projectId = (Long) targetId;
        ProjectRole projectRole = projectMemberService.getAllProjectRolesForUser(projectId, userFromAuth);
        return hasPermission(projectRole, (String) permissionName);
    }

    private boolean hasPermission(ProjectRole projectRole, String permissionName) {
        if (projectRole == null) {
            logger.info("[PERMISSION] Permission denied: project roles is empty");
            return false;
        }
        ProjectPermission projectPermission;
        try {
            projectPermission = ProjectPermission.getPermissionFromName(permissionName);
        } catch (IllegalArgumentException ex) {
            logger.info("[PERMISSION] Permission denied: Unexpected permission name");
            return false;
        }
        return projectRole.getPermissions().contains(projectPermission) || projectRole.equals(OWNER);
    }
}
