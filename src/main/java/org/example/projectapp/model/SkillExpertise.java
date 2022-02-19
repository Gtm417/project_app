package org.example.projectapp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.util.Objects;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "user_skills")
public class SkillExpertise {

    @EmbeddedId
    private UserSkillCompositeKey id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne
    @MapsId("skillId")
    @JoinColumn(name = "skill_id")
    private Skill skill;

    @Column(name = "expertise")
    @Enumerated(value = EnumType.STRING)
    private SkillExpertiseEnum expertise;

    public UserSkillCompositeKey getId() {
        return id;
    }

    public void setId(UserSkillCompositeKey id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Skill getSkill() {
        return skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    public SkillExpertiseEnum getExpertise() {
        return expertise;
    }

    public void setExpertise(SkillExpertiseEnum expertise) {
        this.expertise = expertise;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SkillExpertise)) return false;
        SkillExpertise that = (SkillExpertise) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(user, that.user) &&
                Objects.equals(skill, that.skill) &&
                expertise == that.expertise;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, skill, expertise);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("id", id)
                .append("user", user)
                .append("skill", skill)
                .append("expertise", expertise)
                .toString();
    }
}
