package com.example.notification.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VacancySubscriptionMessageDto extends MessageDto {

    private boolean isSubscribe;
    private Long vacancyId;
    private String userEmail;
    public VacancySubscriptionMessageDto(String name, List<String> receiversEmail, LocalDateTime createdDate,
                                         boolean isSubscribe, Long vacancyId, String userEmail) {
        super(name, receiversEmail, createdDate);
        this.isSubscribe = isSubscribe;
        this.vacancyId = vacancyId;
        this.userEmail = userEmail;
    }

    public VacancySubscriptionMessageDto(String name, List<String> receiversEmail, LocalDateTime createdDate) {
        super(name, receiversEmail, createdDate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("isSubscribe", isSubscribe)
                .append("vacancyId", vacancyId)
                .append("userEmail", userEmail)
                .append("name", name)
                .append("receiversEmail", receiversEmail)
                .append("createdDate", createdDate)
                .toString();
    }
}
