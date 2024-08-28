package com.shop.notification_service.repository;

import com.shop.notification_service.entity.SendEmail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SendEmailRepo extends JpaRepository<SendEmail, String> {
}
