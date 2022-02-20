package org.example.projectapp.service.exception;

public class AlreadyAssignedSkillException extends RuntimeException {
    private final Long userId;
    private final String skillName;

    public AlreadyAssignedSkillException(String message, Long userId, String skillName) {
        super(message);
        this.userId = userId;
        this.skillName = skillName;
    }

    public Long getUserId() {
        return userId;
    }

    public String getSkillName() {
        return skillName;
    }
}
