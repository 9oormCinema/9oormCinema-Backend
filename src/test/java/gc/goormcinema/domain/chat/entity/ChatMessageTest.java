package gc.goormcinema.domain.chat.entity;

import gc.goormcinema.domain.user.entity.User;
import gc.goormcinema.domain.user.entity.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ChatMessage 엔티티 테스트")
class ChatMessageTest {

    private User testUser;
    private ChatRoom testRoom;
    private ChatMessage testMessage;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .email("test@example.com")
                .name("테스트 사용자")
                .phone("010-1234-5678")
                .role(UserRole.USER)
                .isSocial(false)
                .build();

        testRoom = ChatRoom.builder()
                .roomCode("ROOM_001")
                .title("테스트 채팅방")
                .roomType(ChatRoomType.INQUIRY)
                .roomStatus(ChatRoomStatus.ACTIVE)
                .createdBy(testUser)
                .build();

        testMessage = ChatMessage.builder()
                .room(testRoom)
                .sender(testUser)
                .messageType(MessageType.TEXT)
                .content("안녕하세요!")
                .messageStatus(MessageStatus.SENT)
                .isEdited(false)
                .sentAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("ChatMessage 엔티티 생성 테스트")
    void createChatMessage() {
        // given
        String content = "테스트 메시지입니다.";

        // when
        ChatMessage message = ChatMessage.builder()
                .room(testRoom)
                .sender(testUser)
                .messageType(MessageType.TEXT)
                .content(content)
                .messageStatus(MessageStatus.SENT)
                .isEdited(false)
                .sentAt(LocalDateTime.now())
                .build();

        // then
        assertThat(message.getRoom()).isEqualTo(testRoom);
        assertThat(message.getSender()).isEqualTo(testUser);
        assertThat(message.getMessageType()).isEqualTo(MessageType.TEXT);
        assertThat(message.getContent()).isEqualTo(content);
        assertThat(message.getMessageStatus()).isEqualTo(MessageStatus.SENT);
        assertThat(message.getIsEdited()).isFalse();
        assertThat(message.isTextMessage()).isTrue();
        assertThat(message.isFileMessage()).isFalse();
    }

    @Test
    @DisplayName("메시지 수정 테스트")
    void editMessage() {
        // given
        String originalContent = "원본 메시지";
        String editedContent = "수정된 메시지";
        
        ChatMessage message = ChatMessage.builder()
                .room(testRoom)
                .sender(testUser)
                .content(originalContent)
                .messageType(MessageType.TEXT)
                .messageStatus(MessageStatus.SENT)
                .isEdited(false)
                .build();

        LocalDateTime beforeEdit = LocalDateTime.now();

        // when
        message.editMessage(editedContent);

        // then
        assertThat(message.getContent()).isEqualTo(editedContent);
        assertThat(message.getIsEdited()).isTrue();
        assertThat(message.getEditedAt()).isAfter(beforeEdit);
    }

    @Test
    @DisplayName("메시지 읽음 표시 테스트")
    void markAsRead() {
        // given
        ChatMessage message = ChatMessage.builder()
                .room(testRoom)
                .sender(testUser)
                .content("읽음 테스트")
                .messageType(MessageType.TEXT)
                .messageStatus(MessageStatus.SENT)
                .build();

        // when
        message.markAsRead();

        // then
        assertThat(message.getMessageStatus()).isEqualTo(MessageStatus.READ);
    }

    @Test
    @DisplayName("파일 메시지 생성 테스트")
    void createFileMessage() {
        // given
        String fileName = "test.jpg";
        String fileUrl = "https://example.com/files/test.jpg";
        Long fileSize = 1024L;

        // when
        ChatMessage fileMessage = ChatMessage.builder()
                .room(testRoom)
                .sender(testUser)
                .messageType(MessageType.IMAGE)
                .content("이미지를 업로드했습니다.")
                .fileName(fileName)
                .fileUrl(fileUrl)
                .fileSize(fileSize)
                .messageStatus(MessageStatus.SENT)
                .build();

        // then
        assertThat(fileMessage.getMessageType()).isEqualTo(MessageType.IMAGE);
        assertThat(fileMessage.getFileName()).isEqualTo(fileName);
        assertThat(fileMessage.getFileUrl()).isEqualTo(fileUrl);
        assertThat(fileMessage.getFileSize()).isEqualTo(fileSize);
        assertThat(fileMessage.isFileMessage()).isTrue();
        assertThat(fileMessage.isTextMessage()).isFalse();
    }

    @Test
    @DisplayName("답장 메시지 생성 테스트")
    void createReplyMessage() {
        // given
        ChatMessage replyMessage = ChatMessage.builder()
                .room(testRoom)
                .sender(testUser)
                .messageType(MessageType.TEXT)
                .content("답장 메시지입니다.")
                .replyToMessage(testMessage)
                .messageStatus(MessageStatus.SENT)
                .build();

        // when & then
        assertThat(replyMessage.getReplyToMessage()).isEqualTo(testMessage);
        assertThat(replyMessage.getContent()).isEqualTo("답장 메시지입니다.");
    }

    @Test
    @DisplayName("메시지 타입 확인 테스트")
    void messageTypeTest() {
        // given
        ChatMessage textMessage = ChatMessage.builder()
                .messageType(MessageType.TEXT)
                .build();

        ChatMessage imageMessage = ChatMessage.builder()
                .messageType(MessageType.IMAGE)
                .build();

        ChatMessage fileMessage = ChatMessage.builder()
                .messageType(MessageType.FILE)
                .build();

        // when & then
        assertThat(textMessage.isTextMessage()).isTrue();
        assertThat(textMessage.isFileMessage()).isFalse();

        assertThat(imageMessage.isTextMessage()).isFalse();
        assertThat(imageMessage.isFileMessage()).isTrue();

        assertThat(fileMessage.isTextMessage()).isFalse();
        assertThat(fileMessage.isFileMessage()).isTrue();
    }
}