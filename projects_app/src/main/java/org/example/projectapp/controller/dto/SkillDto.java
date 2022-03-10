package org.example.projectapp.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.projectapp.model.SkillExpertiseEnum;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class SkillDto {
    @NotBlank(message = "Should not be empty")
    private String name;
    @NotNull(message = "Should not be empty")
    private SkillExpertiseEnum expertise;
}
