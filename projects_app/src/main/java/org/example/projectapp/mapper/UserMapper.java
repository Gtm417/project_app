package org.example.projectapp.mapper;

import org.example.projectapp.mapper.dto.SkillElasticDto;
import org.example.projectapp.mapper.dto.UserElasticDto;
import org.example.projectapp.model.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserElasticDto convertToUserElasticDto(User user) {
        List<SkillElasticDto> skillsDtoList = getSkillsDto(user);
        return UserElasticDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole())
                .email(user.getEmail())
                .cv(user.getCv())
//                .cv(Arrays.toString(user.getCv()))
                .type(user.getType())
                .status(user.getStatus())
                .description(user.getDescription())
                .skills(skillsDtoList)
                .build();
    }

    private List<SkillElasticDto> getSkillsDto(User user) {
        return user.getSkills().stream()
                .map(x -> new SkillElasticDto(x.getSkill().getName(), x.getExpertise()))
                .collect(Collectors.toList());
    }

}
