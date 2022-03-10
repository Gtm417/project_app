package org.example.projectapp.controller.dto;

import lombok.Builder;
import lombok.Data;
import org.example.projectapp.model.ProjectRole;

import javax.validation.constraints.NotNull;

@Builder
@Data
public class ProjectMemberDto {

    @NotNull(message = "Should not be empty")
    private Long userId;

    @NotNull(message = "Should not be empty")
    ProjectRole role;
}
