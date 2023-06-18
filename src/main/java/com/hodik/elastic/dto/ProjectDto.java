package com.hodik.elastic.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDto {

    private long id;

    private String name;

    private Boolean isPrivate;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate createdDate; //(timestamp)
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate finalPlannedDate;// (timestamp) //– date when project ends
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate startDate; //(timestamp) //– date when project starts

    private String category;

    private String description;

    private String isCommercial;

    private String status;
}
