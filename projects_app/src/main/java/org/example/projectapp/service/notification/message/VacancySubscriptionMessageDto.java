package org.example.projectapp.service.notification.message;

import lombok.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class VacancySubscriptionMessageDto extends MessageDto {
    private boolean isSubscribe;
    private Long vacancyId;
    private String userEmail;

    public VacancySubscriptionMessageDto(String name, List<String> receiversEmail,
                                         boolean isSubscribe, Long vacancyId, String userEmail) {
        super(name, receiversEmail);
        this.isSubscribe = isSubscribe;
        this.vacancyId = vacancyId;
        this.userEmail = userEmail;
    }

    public VacancySubscriptionMessageDto(String name, List<String> receiversEmail) {
        super(name, receiversEmail);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("isSubscribe", isSubscribe)
                .append("vacancyId", vacancyId)
                .append("userEmail", userEmail)
                .append("name", name)
                .append("receiversEmail", receiversEmail)
                .toString();
    }
}
