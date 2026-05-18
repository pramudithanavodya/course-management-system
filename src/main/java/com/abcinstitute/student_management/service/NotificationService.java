package com.abcinstitute.student_management.service;

import com.abcinstitute.student_management.model.Notification;
import com.abcinstitute.student_management.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    // ── Per-student helpers (called from services / controllers) ──

    /** Student registered for the first time */
    public void notifyWelcome(String username) {
        save(username, "WELCOME",
                "🎉 Welcome to ABC Institute! Your journey to greatness starts now.");
    }

    /** Student logged in */
    public void notifyLogin(String username) {
        save(username, "LOGIN",
                "✅ You logged in successfully. Great to have you back!");
    }

    /** Student enrolled in a course */
    public void notifyEnrolled(String username, String courseName) {
        save(username, "ENROLLED",
                "📚 You successfully enrolled in <strong>" + courseName + "</strong>. Time to shine!");
    }

    /** Student unenrolled / dropped a course */
    public void notifyUnenrolled(String username, String courseName) {
        save(username, "UNENROLLED",
                "🚪 You dropped <strong>" + courseName + "</strong>. You can re-enroll any time.");
    }

    // ── NEW: Admin broadcasts sent to ALL students ────────────────

    /**
     * Admin added a brand-new course.
     * Call once per student username from AdminController.
     */
    public void notifyNewCourse(String username, String courseName) {
        save(username, "NEW_COURSE",
                "🆕 A new course <strong>" + courseName
                        + "</strong> is now available. Head to the dashboard to explore and enroll!");
    }

    /**
     * Admin removed an existing course.
     * Call once per student username from AdminController.
     */
    public void notifyCourseRemoved(String username, String courseName) {
        save(username, "COURSE_REMOVED",
                "⚠️ The course <strong>" + courseName
                        + "</strong> has been removed from the institute. Any active enrollment has ended.");
    }

    // ── READ ──────────────────────────────────────────────────────

    /** All notifications for a student, newest first */
    public List<Notification> getNotifications(String username) {
        return notificationRepository
                .findByStudentUsernameOrderByCreatedAtDesc(username);
    }

    /** Unread count — used for the navbar badge */
    public long getUnreadCount(String username) {
        return notificationRepository
                .countByStudentUsernameAndReadFalse(username);
    }

    // ── UPDATE ────────────────────────────────────────────────────

    /** Mark every notification as read when the user opens the panel */
    public void markAllRead(String username) {
        notificationRepository.markAllAsRead(username);
    }

    // ── Private helper ────────────────────────────────────────────
    private void save(String username, String type, String message) {
        notificationRepository.save(new Notification(username, type, message));
    }
}
