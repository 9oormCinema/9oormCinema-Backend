package gc.goormcinema.domain.chat.repository;

import gc.goormcinema.domain.chat.entity.ChatNotification;
import gc.goormcinema.domain.chat.entity.NotificationType;
import gc.goormcinema.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ChatNotificationRepository extends JpaRepository<ChatNotification, Long> {

    List<ChatNotification> findByUserAndIsReadOrderByCreatedAtDesc(User user, Boolean isRead);

    Page<ChatNotification> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);

    @Query("SELECT cn FROM ChatNotification cn WHERE cn.user = :user AND cn.isRead = false AND (cn.expiresAt IS NULL OR cn.expiresAt > :now)")
    List<ChatNotification> findUnreadAndNotExpiredByUser(@Param("user") User user, @Param("now") LocalDateTime now);

    @Query("SELECT COUNT(cn) FROM ChatNotification cn WHERE cn.user = :user AND cn.isRead = false")
    Long countUnreadByUser(@Param("user") User user);

    List<ChatNotification> findByRoomIdAndNotificationType(Long roomId, NotificationType notificationType);

    @Query("SELECT cn FROM ChatNotification cn WHERE cn.expiresAt < :now AND cn.isRead = false")
    List<ChatNotification> findExpiredNotifications(@Param("now") LocalDateTime now);

    @Query("SELECT cn FROM ChatNotification cn WHERE cn.isPushSent = false AND cn.isRead = false ORDER BY cn.createdAt ASC")
    List<ChatNotification> findUnsentPushNotifications();
}