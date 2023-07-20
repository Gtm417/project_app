package com.hodik.elastic.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Skill {
    @JsonProperty("skillName")
    private String skillName;
    @JsonProperty("expertise")
    private Expertise expertise;

}
