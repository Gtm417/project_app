package org.example.projectapp.controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FilterDto {
    @NotBlank(message = "Should not be empty")
    private String name;
    private SearchOperation operation;
    private List<Object> values;
    private DataType dataType;
    private boolean orPredicate;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("name", name)
                .append("operation", operation)
                .append("values", values)
                .append("dataType", dataType)
                .append("orPredicate", orPredicate)
                .toString();
    }
}
