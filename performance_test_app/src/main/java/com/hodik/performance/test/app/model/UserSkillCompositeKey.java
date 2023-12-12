package com.hodik.performance.test.app.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Objects;

@Data
@Setter
@Getter
@Embeddable
public class UserSkillCompositeKey implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "skill_id")
    private Long skillId;

    public UserSkillCompositeKey() {
    }

    public UserSkillCompositeKey(Long userId, Long skillId) {
        this.userId = userId;
        this.skillId = skillId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserSkillCompositeKey)) return false;
        UserSkillCompositeKey that = (UserSkillCompositeKey) o;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(skillId, that.skillId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, skillId);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("userId", userId)
                .append("skillId", skillId)
                .toString();
    }
}
