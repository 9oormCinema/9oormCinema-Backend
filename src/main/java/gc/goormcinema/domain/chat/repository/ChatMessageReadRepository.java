package gc.goormcinema.domain.chat.repository;

import gc.goormcinema.domain.chat.entity.ChatMessageRead;
import gc.goormcinema.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatMessageReadRepository extends JpaRepository<ChatMessageRead, Long> {

    Optional<ChatMessageRead> findByMessageIdAndUser(Long messageId, User user);

    List<ChatMessageRead> findByMessageId(Long messageId);

    List<ChatMessageRead> findByUser(User user);

    @Query("SELECT cmr FROM ChatMessageRead cmr WHERE cmr.message.room.id = :roomId AND cmr.user = :user ORDER BY cmr.readAt DESC")
    List<ChatMessageRead> findByRoomIdAndUserOrderByReadAtDesc(@Param("roomId") Long roomId, @Param("user") User user);

    @Query("SELECT COUNT(DISTINCT cmr.user) FROM ChatMessageRead cmr WHERE cmr.message.id = :messageId")
    Long countDistinctReadersByMessageId(@Param("messageId") Long messageId);

    @Query("SELECT cmr.user FROM ChatMessageRead cmr WHERE cmr.message.id = :messageId")
    List<User> findUsersByMessageId(@Param("messageId") Long messageId);
}