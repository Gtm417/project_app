package org.example.projectapp.controller;

import org.example.projectapp.controller.dto.SkillDto;
import org.example.projectapp.service.SkillsService;
import org.example.projectapp.service.dto.UserSkillDto;
import org.example.projectapp.service.exception.AlreadyAssignedSkillException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/users/{userId}/skills")
public class SkillController {
    private static final Logger logger = LoggerFactory.getLogger(SkillController.class);
    private final SkillsService skillsService;

    public SkillController(SkillsService skillsService) {
        this.skillsService = skillsService;
    }

    @PostMapping
    public ResponseEntity<UserSkillDto> addSkill(@PathVariable Long userId, @RequestBody @Valid SkillDto skillDto) {
        UserSkillDto body = skillsService.addSkill(skillDto.getName(), skillDto.getExpertise(), userId);
        return ResponseEntity.ok(body);
    }

    @DeleteMapping("/{skillId}")
    public ResponseEntity<?> removeSkill(@PathVariable Long userId, @PathVariable Long skillId) {
        skillsService.removeSkill(userId, skillId);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(AlreadyAssignedSkillException.class)
    public ResponseEntity<String> alreadyAssignedSkillHandling(AlreadyAssignedSkillException e) {
        logger.info("[SKILL] User {} already have such skill {}", e.getUserId(), e.getSkillName());
        //todo reconsider handling after frontend
        return ResponseEntity.badRequest().body("User already have such skill");
    }

}
