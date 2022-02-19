package org.example.projectapp.repository;

import org.example.projectapp.model.SkillExpertise;
import org.example.projectapp.model.UserSkillCompositeKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillExpertiseRepository extends JpaRepository<SkillExpertise, UserSkillCompositeKey> {
}
