package org.example.projectapp.mapper.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.example.projectapp.model.ProjectStatus;
import org.example.projectapp.util.CustomLocalDateTimeSerializer;

import java.time.LocalDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectElasticDto {

    private long id;
    private String name;
    private Boolean isPrivate;
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime createdDate;
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime finalPlannedDate;
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime startDate;
    private String category;
    private String description;
    private Boolean isCommercial;
    private ProjectStatus status;

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
