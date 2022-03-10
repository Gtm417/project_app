package org.example.projectapp.repository;

import org.example.projectapp.model.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillRepository extends JpaRepository<Skill, Long> {
    Skill findByName(String name);
}
