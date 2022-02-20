package org.example.projectapp.service.dto;

import lombok.Builder;
import lombok.Data;
import org.example.projectapp.model.ProjectStatus;

import java.time.LocalDateTime;

@Builder
@Data
public class ProjectResponseDto {
    private Long id;
    private String name;
    private boolean isPrivate;
    private LocalDateTime createDate;
    private LocalDateTime scheduledEndDate;
    private LocalDateTime startDate;
    private String category;
    private String description;
    private ProjectStatus status;
    private boolean isCommercial;
}
