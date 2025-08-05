package gc.goormcinema.domain.chat.entity;

import gc.goormcinema.domain.user.entity.User;
import gc.goormcinema.global.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_messages")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ChatMessage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private ChatRoom room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @Enumerated(EnumType.STRING)
    @Column(name = "message_type", length = 20)
    @Builder.Default
    private MessageType messageType = MessageType.TEXT;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "file_url", length = 500)
    private String fileUrl;

    @Column(name = "file_name", length = 255)
    private String fileName;

    @Column(name = "file_size")
    private Long fileSize;

    @Enumerated(EnumType.STRING)
    @Column(name = "message_status", length = 20)
    @Builder.Default
    private MessageStatus messageStatus = MessageStatus.SENT;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reply_to_message_id")
    private ChatMessage replyToMessage;

    @Column(name = "mentions", columnDefinition = "JSON")
    private String mentions;

    @Column(name = "is_edited")
    @Builder.Default
    private Boolean isEdited = false;

    @Column(name = "sent_at", nullable = false)
    private LocalDateTime sentAt;

    @Column(name = "edited_at")
    private LocalDateTime editedAt;

    // 비즈니스 메서드
    public void editMessage(String newContent) {
        this.content = newContent;
        this.isEdited = true;
        this.editedAt = LocalDateTime.now();
    }

    public void markAsRead() {
        this.messageStatus = MessageStatus.READ;
    }

    public boolean isTextMessage() {
        return this.messageType == MessageType.TEXT;
    }

    public boolean isFileMessage() {
        return this.messageType == MessageType.FILE || this.messageType == MessageType.IMAGE;
    }

    @PrePersist
    protected void onCreate() {
        if (sentAt == null) {
            sentAt = LocalDateTime.now();
        }
    }
}