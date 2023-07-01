package com.hodik.elastic.dto;

import com.hodik.elastic.model.Skill;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    private long id;

    @Email(regexp = ".+@.+\\..+|", message = "Provide correct email")
    private String email;

    @NotEmpty(message = "Password can't ce empty")
    @Size(min = 4, max = 15, message = "2-25 letters")
    private String password;

    @NotEmpty(message = "Enter the name")
    @Pattern(regexp = "[A-Z А-Я]\\\\w+", message = "Example : Misha")
    private String firstName;

    @NotEmpty(message = "Enter the last name")
    @Pattern(regexp = "[A-Z А-Я]\\\\w+", message = "Example : Misha")
    private String lastName;

    private String role;

    private String description;

    private String status;

    private String type;

    String cv;// probably just text (not sure yet)

    private List<Skill> skill; //(nested indexable field)
}
