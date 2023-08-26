package org.example.projectapp.service.notification.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
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
                .append("createDate", createDate)
                .toString();
    }
}
