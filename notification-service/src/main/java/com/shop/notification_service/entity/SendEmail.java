package com.shop.notification_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;


@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SendEmail {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String subject;
    private String htmlContent;
    private String messageId;
    private String sender;

}
