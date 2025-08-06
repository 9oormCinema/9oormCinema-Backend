package gc.goormcinema.domain.chat.repository;

import gc.goormcinema.domain.chat.entity.ChatMessage;
import gc.goormcinema.domain.chat.entity.MessageStatus;
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
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    Page<ChatMessage> findByRoomIdOrderBySentAtDesc(Long roomId, Pageable pageable);

    List<ChatMessage> findByRoomIdAndSentAtAfterOrderBySentAtAsc(Long roomId, LocalDateTime afterTime);

    @Query("SELECT cm FROM ChatMessage cm WHERE cm.room.id = :roomId AND cm.sender = :sender ORDER BY cm.sentAt DESC")
    List<ChatMessage> findByRoomIdAndSenderOrderBySentAtDesc(@Param("roomId") Long roomId, @Param("sender") User sender);

    @Query("SELECT COUNT(cm) FROM ChatMessage cm WHERE cm.room.id = :roomId AND cm.sentAt > :afterTime")
    Long countByRoomIdAndSentAtAfter(@Param("roomId") Long roomId, @Param("afterTime") LocalDateTime afterTime);

    @Query("SELECT cm FROM ChatMessage cm WHERE cm.room.id = :roomId AND cm.messageStatus = :status ORDER BY cm.sentAt ASC")
    List<ChatMessage> findByRoomIdAndMessageStatusOrderBySentAtAsc(@Param("roomId") Long roomId, @Param("status") MessageStatus status);

    @Query("SELECT cm FROM ChatMessage cm WHERE cm.sender = :sender AND cm.sentAt BETWEEN :startTime AND :endTime ORDER BY cm.sentAt DESC")
    List<ChatMessage> findBySenderAndSentAtBetween(@Param("sender") User sender, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
}