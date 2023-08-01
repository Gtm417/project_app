package org.example.projectapp.mapper.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.example.projectapp.model.SkillExpertiseEnum;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SkillElasticDto {

    private String skillName;

    private SkillExpertiseEnum expertise;


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("skillName", skillName)
                .append("expertise", expertise)
                .toString();
    }
}
