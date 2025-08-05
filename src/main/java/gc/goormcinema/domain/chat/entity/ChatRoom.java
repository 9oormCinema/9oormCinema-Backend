package gc.goormcinema.domain.chat.entity;

import gc.goormcinema.domain.user.entity.User;
import gc.goormcinema.global.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "chat_rooms")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ChatRoom extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "room_code", nullable = false, unique = true, length = 50)
    private String roomCode;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "room_type", length = 20)
    @Builder.Default
    private ChatRoomType roomType = ChatRoomType.INQUIRY;

    @Enumerated(EnumType.STRING)
    @Column(name = "room_status", length = 20)
    @Builder.Default
    private ChatRoomStatus roomStatus = ChatRoomStatus.ACTIVE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_admin")
    private User assignedAdmin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "closed_by")
    private User closedBy;

    @Column(name = "closed_at")
    private java.time.LocalDateTime closedAt;

    @Column(name = "last_message_id")
    private Long lastMessageId;

    @Column(name = "last_message_at")
    private java.time.LocalDateTime lastMessageAt;

    @Column(name = "total_message_count")
    @Builder.Default
    private Integer totalMessageCount = 0;

    // 비즈니스 메서드
    public void closeRoom(User closedBy) {
        this.roomStatus = ChatRoomStatus.CLOSED;
        this.closedBy = closedBy;
        this.closedAt = java.time.LocalDateTime.now();
    }

    public void updateLastMessage(Long messageId) {
        this.lastMessageId = messageId;
        this.lastMessageAt = java.time.LocalDateTime.now();
        this.totalMessageCount++;
    }

    public void assignAdmin(User admin) {
        this.assignedAdmin = admin;
    }

    public boolean isActive() {
        return this.roomStatus == ChatRoomStatus.ACTIVE;
    }
}