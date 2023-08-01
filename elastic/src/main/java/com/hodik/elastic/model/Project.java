package com.hodik.elastic.model;

import com.hodik.elastic.util.CustomDatePropertyValueConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.ValueConverter;

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
    @Field(type = FieldType.Keyword)
    private String name;
    @Field(type = FieldType.Boolean)
    private boolean isPrivate;
    @Field(type = FieldType.Date)
    @ValueConverter(CustomDatePropertyValueConverter.class)
    private LocalDateTime createdDate; //(timestamp)

    @Field(type = FieldType.Date)
    @ValueConverter(CustomDatePropertyValueConverter.class)
    private LocalDateTime finalPlannedDate;// (timestamp) //– date when project ends

    @Field(type = FieldType.Date)
    @ValueConverter(CustomDatePropertyValueConverter.class)
    private LocalDateTime startDate; //(timestamp) //– date when project starts

    @Field(type = FieldType.Keyword)
    private String category;
    @Field(type = FieldType.Text)
    private String description;
    @Field(type = FieldType.Boolean)
    private Boolean isCommercial;
    @Field(type = FieldType.Keyword)
    private ProjectStatus status;
}
