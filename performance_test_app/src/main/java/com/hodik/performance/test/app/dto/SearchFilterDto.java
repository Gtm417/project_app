package com.hodik.performance.test.app.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class SearchFilterDto {
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
