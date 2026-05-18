package com.abcinstitute.student_management.repository;

import com.abcinstitute.student_management.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // Get all notifications for a user, newest first
    List<Notification> findByStudentUsernameOrderByCreatedAtDesc(String username);

    // Count unread notifications for a user (used for the badge counter)
    long countByStudentUsernameAndReadFalse(String username);

    // Mark all notifications as read for a user
    @Modifying
    @Transactional
    @Query("UPDATE Notification n SET n.read = true WHERE n.studentUsername = :username")
    void markAllAsRead(String username);
}
