package org.example.projectapp.service.dto;

import lombok.Builder;
import lombok.Data;
import org.example.projectapp.model.SkillExpertiseEnum;

@Builder
@Data
public class UserSkillDto {
    private Long userId;
    private Long skillId;
    private String skillName;
    private SkillExpertiseEnum expertise;
}
