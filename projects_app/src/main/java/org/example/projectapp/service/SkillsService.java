package org.example.projectapp.service;

import org.example.projectapp.controller.dto.SkillDto;
import org.example.projectapp.model.SkillExpertiseEnum;
import org.example.projectapp.service.dto.UserSkillDto;

import javax.validation.Valid;

public interface SkillsService {
    UserSkillDto addSkill(String name, SkillExpertiseEnum expertise, Long userId);

    void removeSkill(Long userId, Long skillId);

    UserSkillDto updateSkill(Long userId, Long skillId, @Valid SkillDto skillDto);
}
