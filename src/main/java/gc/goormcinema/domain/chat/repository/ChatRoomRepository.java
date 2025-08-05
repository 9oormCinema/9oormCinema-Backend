package gc.goormcinema.domain.chat.repository;

import gc.goormcinema.domain.chat.entity.ChatRoom;
import gc.goormcinema.domain.chat.entity.ChatRoomStatus;
import gc.goormcinema.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    Optional<ChatRoom> findByRoomCode(String roomCode);

    List<ChatRoom> findByCreatedByAndRoomStatus(User createdBy, ChatRoomStatus roomStatus);

    List<ChatRoom> findByAssignedAdminAndRoomStatus(User assignedAdmin, ChatRoomStatus roomStatus);

    @Query("SELECT cr FROM ChatRoom cr WHERE cr.createdBy = :user OR cr.assignedAdmin = :user")
    Page<ChatRoom> findByUserWithPaging(@Param("user") User user, Pageable pageable);

    @Query("SELECT cr FROM ChatRoom cr WHERE cr.roomStatus = :status ORDER BY cr.lastMessageAt DESC")
    List<ChatRoom> findByRoomStatusOrderByLastMessageAtDesc(@Param("status") ChatRoomStatus status);

    @Query("SELECT COUNT(cr) FROM ChatRoom cr WHERE cr.createdBy = :user AND cr.roomStatus = :status")
    Long countByCreatedByAndRoomStatus(@Param("user") User user, @Param("status") ChatRoomStatus status);
}