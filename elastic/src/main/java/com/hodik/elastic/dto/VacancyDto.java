package com.hodik.elastic.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VacancyDto {
    private Long id;
    private Long projectId;
    private String creator;
    private String description;
    private String aboutProject;
    String expected;
    private String jobPosition;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("id", id)
                .append("skillId", projectId)
                .append("creator", creator)
                .append("description", description)
                .append("aboutProject", aboutProject)
                .append("expected", expected)
                .append("jobPosition", jobPosition)
                .toString();
    }
}
