package com.hodik.elastic.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "projects")
public class Project {
    @Id
    @UniqueElements
    @Field(type = FieldType.Long)
    private long id;
    @Field(type = FieldType.Text)
    private String name;
    @Field(type = FieldType.Boolean)
    private Boolean isPrivate;
    @Field(type = FieldType.Date)
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate createdDate; //(timestamp)
    @Field(type = FieldType.Date)
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate finalPlannedDate;// (timestamp) //– date when project ends
    @Field(type = FieldType.Date)
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate startDate; //(timestamp) //– date when project starts
    @Field(type = FieldType.Text)
    private String category;
    @Field(type = FieldType.Text)
    private String description;
    @Field(type = FieldType.Text)
    private String isCommercial;
    @Field(type = FieldType.Text)
    private String status;
}
