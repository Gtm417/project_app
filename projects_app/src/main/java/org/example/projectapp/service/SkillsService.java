package org.example.projectapp.service;

import org.example.projectapp.model.SkillExpertiseEnum;
import org.example.projectapp.service.dto.UserSkillDto;

public interface SkillsService {
    UserSkillDto addSkill(String name, SkillExpertiseEnum expertise, Long userId);

    void removeSkill(Long userId, Long skillId);
}
