package gc.goormcinema.domain.chat.repository;

import gc.goormcinema.domain.chat.entity.ChatParticipant;
import gc.goormcinema.domain.chat.entity.ParticipantRole;
import gc.goormcinema.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, Long> {

    Optional<ChatParticipant> findByRoomIdAndUser(Long roomId, User user);

    List<ChatParticipant> findByRoomIdAndIsActive(Long roomId, Boolean isActive);

    List<ChatParticipant> findByUserAndIsActive(User user, Boolean isActive);

    @Query("SELECT cp FROM ChatParticipant cp WHERE cp.room.id = :roomId AND cp.participantRole = :role AND cp.isActive = true")
    List<ChatParticipant> findByRoomIdAndParticipantRoleAndIsActive(@Param("roomId") Long roomId, @Param("role") ParticipantRole role);

    @Query("SELECT cp FROM ChatParticipant cp WHERE cp.user = :user AND cp.isActive = true AND cp.unreadCount > 0")
    List<ChatParticipant> findByUserWithUnreadMessages(@Param("user") User user);

    @Query("SELECT SUM(cp.unreadCount) FROM ChatParticipant cp WHERE cp.user = :user AND cp.isActive = true")
    Long getTotalUnreadCountByUser(@Param("user") User user);

    @Query("SELECT COUNT(cp) FROM ChatParticipant cp WHERE cp.room.id = :roomId AND cp.isActive = true")
    Long countActiveParticipantsByRoomId(@Param("roomId") Long roomId);
}