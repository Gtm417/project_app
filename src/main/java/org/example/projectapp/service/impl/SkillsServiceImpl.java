package org.example.projectapp.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.example.projectapp.auth.AuthService;
import org.example.projectapp.model.*;
import org.example.projectapp.repository.SkillExpertiseRepository;
import org.example.projectapp.repository.SkillRepository;
import org.example.projectapp.service.SkillsService;
import org.example.projectapp.service.dto.UserSkillDto;
import org.example.projectapp.service.exception.AlreadyAssignedSkillException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SkillsServiceImpl implements SkillsService {
    private static final Logger logger = LoggerFactory.getLogger(SkillsServiceImpl.class);

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
    public UserSkillDto addSkill(String name, SkillExpertiseEnum expertise, Long userId) {
        User userFromAuth = authService.getUserFromAuth();
        checkAccess(userId, userFromAuth);

        boolean alreadyAssigned = userFromAuth.getSkills().stream()
                .anyMatch(s -> StringUtils.equalsIgnoreCase(s.getSkill().getName(), name));
        if (alreadyAssigned) {
            throw new AlreadyAssignedSkillException("User already have such skill", userFromAuth.getId(), name);
        }

        Skill skill = skillRepository.findByName(name);
        if (skill == null) {
            skill = skillRepository.saveAndFlush(Skill.builder().name(name).build());
        }

        return saveAndReturn(expertise, userFromAuth, skill);
    }

    private void checkAccess(Long userId, User userFromAuth) {
        if (!userFromAuth.getId().equals(userId)) {
            logger.info("Authorized user={} doesn't have permission to update user={}", userFromAuth.getId(), userId);
            throw new UnsupportedOperationException("Cannot change another user");
        }
    }

    public void removeSkill(Long userId, Long skillId) {
        User userFromAuth = authService.getUserFromAuth();
        checkAccess(userId, userFromAuth);
        UserSkillCompositeKey id = new UserSkillCompositeKey(userId, skillId);
        skillExpertiseRepository.findById(id)
                .ifPresent(skillExpertiseRepository::delete);
    }

    private UserSkillDto saveAndReturn(SkillExpertiseEnum expertise, User userFromAuth, Skill skill) {
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
