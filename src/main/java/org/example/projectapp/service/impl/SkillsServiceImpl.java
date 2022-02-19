package org.example.projectapp.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.example.projectapp.auth.AuthService;
import org.example.projectapp.model.*;
import org.example.projectapp.repository.SkillExpertiseRepository;
import org.example.projectapp.repository.SkillRepository;
import org.example.projectapp.service.SkillsService;
import org.example.projectapp.service.dto.UserSkillDto;
import org.example.projectapp.service.exception.AlreadyAssignedSkillException;
import org.springframework.stereotype.Service;

@Service
public class SkillsServiceImpl implements SkillsService {
    private final SkillRepository skillRepository;
    private final SkillExpertiseRepository skillExpertiseRepository;
    private final AuthService authService;

    public SkillsServiceImpl(SkillRepository skillRepository,
                             SkillExpertiseRepository skillExpertiseRepository,
                             AuthService authService) {
        this.skillRepository = skillRepository;
        this.skillExpertiseRepository = skillExpertiseRepository;
        this.authService = authService;
    }

    @Override
    public UserSkillDto addSkill(String name, SkillExpertiseEnum expertise) {
        User userFromAuth = authService.getUserFromAuth();
        Skill skill = skillRepository.findByName(name);
        if (skill == null) {
            skill = skillRepository.saveAndFlush(Skill.builder().name(name).build());
        }

        boolean alreadyAssigned = userFromAuth.getSkills().stream()
                .anyMatch(s -> StringUtils.equalsIgnoreCase(s.getSkill().getName(), name));
        if (alreadyAssigned) {
            throw new AlreadyAssignedSkillException("User already have such skill", userFromAuth.getId(), skill.getId());
        }

        skillExpertiseRepository.save(SkillExpertise.builder()
                .id(new UserSkillCompositeKey(userFromAuth.getId(), skill.getId()))
                .expertise(expertise)
                .skill(skill)
                .user(userFromAuth)
                .build());
        return UserSkillDto.builder()
                .skillId(skill.getId())
                .skillName(skill.getName())
                .userId(userFromAuth.getId())
                .expertise(expertise)
                .build();
    }
}
