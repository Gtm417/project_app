package com.hodik.elastic.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(indexName = "vacancies")
public class Vacancy {
    @Id
    @UniqueElements
    @Field(type = FieldType.Long)
    private Long id;

    @UniqueElements
    @Field(type = FieldType.Long)
    private Long projectId;
    @Field(type = FieldType.Keyword)
    private String creator;
    @Field(type = FieldType.Keyword)
    private String description;
    @Field(type = FieldType.Keyword)
    private String aboutProject;
    @Field(type = FieldType.Keyword)
    String expected;
    @Field(type = FieldType.Keyword)
    private String jobPosition;


}
