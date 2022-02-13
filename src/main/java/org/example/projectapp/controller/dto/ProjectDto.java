package org.example.projectapp.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.projectapp.model.ProjectStatus;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ProjectDto {
    @NotBlank(message = "Should not be empty")
    private String name;

    @NotBlank(message = "Should not be empty")
    private String category;

    @NotNull(message = "Should not be empty")
    private Boolean isPrivate;

    @Future(message = "end date has to be in future")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime scheduledEndDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime startDate;

    private ProjectStatus status;

    @NotNull(message = "Should not be empty")
    private Boolean isCommercial;

}
