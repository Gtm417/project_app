package org.example.projectapp.service.exception;

public class AlreadyAssignedSkillException extends RuntimeException {
    private final Long userId;
    private final Long skillId;

    public AlreadyAssignedSkillException(String message, Long userId, Long skillId) {
        super(message);
        this.userId = userId;
        this.skillId = skillId;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getSkillId() {
        return skillId;
    }
}
