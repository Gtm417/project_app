package org.example.projectapp.service.dto;

import lombok.Builder;
import lombok.Data;
import org.example.projectapp.model.ProjectRole;

import java.time.LocalDateTime;

@Builder
@Data
public class ProjectMemberResponseDto {

    private Long id;
    private Long userId;
    private Long projectId;
    private ProjectRole projectRole;
    private LocalDateTime addedAt;
}
