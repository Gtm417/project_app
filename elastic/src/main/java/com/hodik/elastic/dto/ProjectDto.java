package com.hodik.elastic.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hodik.elastic.model.ProjectStatus;
import com.hodik.elastic.util.CustomLocalDateTimeDeSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDto {

    private long id;

    private String name;

    private Boolean isPrivate;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @JsonDeserialize(using = CustomLocalDateTimeDeSerializer.class)
    private LocalDateTime createdDate;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @JsonDeserialize(using = CustomLocalDateTimeDeSerializer.class)
    private LocalDateTime finalPlannedDate;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @JsonDeserialize(using = CustomLocalDateTimeDeSerializer.class)
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
