package com.example.notification.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notifiication_events")
public class NotificationEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_name", nullable = false)
    private String eventName;

    @Column(name="queue", nullable = false)
    private String queue;

    @Column(name="message_date", nullable = false)
    private LocalDateTime messageDate;

    @Column(name="receive_date", nullable = false)
    private LocalDateTime receiveDate;

    @Column(name="processed_date")
    private LocalDateTime processedDate;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "event_status",nullable = false)
    private EventStatus status;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("id", id)
                .append("eventName", eventName)
                .append("queue", queue)
                .append("messageDate", messageDate)
                .append("receiveDate", receiveDate)
                .append("processedDate", processedDate)
                .append("status", status)
                .toString();
    }
}
