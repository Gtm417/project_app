package com.hodik.elastic.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.LocalDate;

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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("id", id)
                .append("name", name)
                .append("isPrivate", isPrivate)
                .append("createdDate", createdDate)
                .append("finalPlannedDate", finalPlannedDate)
                .append("startDate", startDate)
                .append("category", category)
                .append("description", description)
                .append("isCommercial", isCommercial)
                .append("status", status)
                .toString();
    }
}
