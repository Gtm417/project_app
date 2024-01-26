package org.example.projectapp.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.projectapp.controller.dto.SkillDto;
import org.example.projectapp.model.Status;
import org.example.projectapp.model.UserType;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private Status status;
    private UserType type;
    private byte[] picture;
    private byte[] cv;
    private List<SkillDto> skills;
}
