package org.example.projectapp.mapper.dto;

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
public class VacancyElasticDto {
    private Long id;
    private Long projectId;
    private String creator;// who created vacancy
    private String description; // vacancy description
    private String aboutProject; // about project text
    String expected; // what’s expected from candidate
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