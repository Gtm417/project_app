package com.hodik.elastic.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Skill {
    @JsonProperty("skillName")
    @Field(type = FieldType.Keyword)
    private String skillName;
    @JsonProperty("expertise")
    @Field(type = FieldType.Keyword)
    private Expertise expertise;

}
