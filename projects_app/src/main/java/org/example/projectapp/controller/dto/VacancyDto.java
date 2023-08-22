package org.example.projectapp.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VacancyDto {
    private String description;
    private String aboutProject;
    private String expected;
    @NotBlank(message = "Should not be empty")
    private String jobPosition;
    @NotNull(message = "Should not be empty")
    @Digits(integer = Integer.MAX_VALUE, fraction = 0, message = "Should be without fractional part")
    private long projectId;
}
