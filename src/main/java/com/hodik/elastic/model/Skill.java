package com.hodik.elastic.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;

@JsonSerialize
public class Skill {
    @JsonProperty("skillName")
    private String skillName;
    @JsonProperty("expertise")
    private Expertise expertise;

}
