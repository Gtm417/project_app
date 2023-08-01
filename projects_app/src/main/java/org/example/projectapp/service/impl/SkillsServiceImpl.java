package org.example.projectapp.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.example.projectapp.auth.AuthService;
import org.example.projectapp.mapper.UserMapper;
import org.example.projectapp.mapper.dto.UserElasticDto;
import org.example.projectapp.model.*;
import org.example.projectapp.repository.SkillExpertiseRepository;
import org.example.projectapp.repository.SkillRepository;
import org.example.projectapp.restclient.ElasticUsersServiceClient;
import org.example.projectapp.service.SkillsService;
import org.example.projectapp.service.dto.UserSkillDto;
import org.example.projectapp.service.exception.AlreadyAssignedSkillException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class SkillsServiceImpl implements SkillsService {
    private static final Logger logger = LoggerFactory.getLogger(SkillsServiceImpl.class);

    private final SkillRepository skillRepository;
    private final SkillExpertiseRepository skillExpertiseRepository;
    private final AuthService authService;
    private final ElasticUsersServiceClient elasticUsersServiceClient;
    private final UserMapper userMapper;

    public SkillsServiceImpl(SkillRepository skillRepository,
                             SkillExpertiseRepository skillExpertiseRepository,
                             AuthService authService, ElasticUsersServiceClient elasticUsersServiceClient, UserMapper userMapper) {
        this.skillRepository = skillRepository;
        this.skillExpertiseRepository = skillExpertiseRepository;
        this.authService = authService;
        this.elasticUsersServiceClient = elasticUsersServiceClient;
        this.userMapper = userMapper;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
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

        SkillExpertise skillSaved = saveSkillExpertise(expertise, userFromAuth, skill);
        userFromAuth.getSkills().add(skillSaved);
        synchroniseUserToElastic(userFromAuth);
        return mapToDto(skillSaved);
    }

    private void synchroniseUserToElastic(User user) {
        UserElasticDto userElasticDto = userMapper.convertToUserElasticDto(user);
        elasticUsersServiceClient.updateUser(user.getId(), userElasticDto);
    }

    private void checkAccess(Long userId, User userFromAuth) {
        if (!userFromAuth.getId().equals(userId)) {
            logger.info("Authorized user={} doesn't have permission to update user={}", userFromAuth.getId(), userId);
            throw new UnsupportedOperationException("Cannot change another user");
        }
    }

    @Transactional
    public void removeSkill(Long userId, Long skillId) {
        User userFromAuth = authService.getUserFromAuth();
        checkAccess(userId, userFromAuth);
        UserSkillCompositeKey id = new UserSkillCompositeKey(userId, skillId);
        Optional<SkillExpertise> skillById = skillExpertiseRepository.findById(id);
        if (skillById.isPresent()) {
            SkillExpertise skillExpertise = skillById.get();
            skillExpertiseRepository.delete(skillExpertise);
            userFromAuth.getSkills().remove(skillExpertise);
        }
        synchroniseUserToElastic(userFromAuth);
    }

    private UserSkillDto mapToDto(SkillExpertise skillExpertise) {
        Skill skillInDb = skillExpertise.getSkill();
        return UserSkillDto.builder()
                .skillId(skillInDb.getId())
                .skillName(skillInDb.getName())
                .userId(skillExpertise.getUser().getId())
                .expertise(skillExpertise.getExpertise())
                .build();
    }

    private SkillExpertise saveSkillExpertise(SkillExpertiseEnum expertise, User userFromAuth, Skill skill) {
        return skillExpertiseRepository.saveAndFlush(SkillExpertise.builder()
                .id(new UserSkillCompositeKey(userFromAuth.getId(), skill.getId()))
                .expertise(expertise)
                .skill(skill)
                .user(userFromAuth)
                .build());
    }
}
