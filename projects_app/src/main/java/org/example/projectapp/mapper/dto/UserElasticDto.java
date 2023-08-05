package org.example.projectapp.mapper.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.example.projectapp.model.Role;
import org.example.projectapp.model.Status;
import org.example.projectapp.model.UserType;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserElasticDto {
    private long id;
    private String email;
    private String firstName;
    private String lastName;
    private Role role;
    private String description;
    private Status status;
    private UserType type;
    private String cv;// probably just text (not sure yet)
    private List<SkillElasticDto> skills; //(nested indexable field)

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("id", id)
                .append("email", email)
                .append("firstName", firstName)
                .append("lastName", lastName)
                .append("role", role)
                .append("description", description)
                .append("status", status)
                .append("type", type)
                .append("cv", cv)
                .append("skills", skills)
                .toString();
    }
}


