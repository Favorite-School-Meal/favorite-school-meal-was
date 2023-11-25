package com.example.favoriteschoolmeal.domain.notification.repository;

import com.example.favoriteschoolmeal.domain.notification.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

}